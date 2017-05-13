package com.example.jiefly.multiparametermonitor.connection.ble;

import android.app.Service;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.example.jiefly.multiparametermonitor.connection.Connection;
import com.example.jiefly.multiparametermonitor.connection.OnConnectionListener;
import com.example.jiefly.multiparametermonitor.sensor.CJMCU;
import com.qindachang.bluetoothle.BluetoothLe;
import com.qindachang.bluetoothle.OnLeConnectListener;
import com.qindachang.bluetoothle.OnLeNotificationListener;
import com.qindachang.bluetoothle.OnLeReadCharacteristicListener;
import com.qindachang.bluetoothle.OnLeWriteCharacteristicListener;
import com.qindachang.bluetoothle.exception.BleException;
import com.qindachang.bluetoothle.exception.ConnBleException;
import com.qindachang.bluetoothle.exception.ReadBleException;
import com.qindachang.bluetoothle.exception.WriteBleException;

import java.util.UUID;

public class BleConnectionService extends Service implements Connection {
    private static final String TAG = "BleConnectionService";
    private BluetoothLe mBluetoothLe;
    private OnConnectionListener mOnConnectionListener;
    private BluetoothGatt mIOGatt;
    private BluetoothGattCharacteristic mIOCharacteristic;

    public BleConnectionService() {
        mBluetoothLe = BluetoothLe.getDefault();
        mBluetoothLe.setOnConnectListener(new OnLeConnectListener() {
            @Override
            public void onDeviceConnecting() {
                Log.i(TAG, "设备连接中...");
                mOnConnectionListener.onDeviceConnecting();
            }

            @Override
            public void onDeviceConnected() {
                Log.i(TAG, "设备连接成功");
                mOnConnectionListener.onDeviceConnected();
            }

            @Override
            public void onDeviceDisconnected() {
                Log.i(TAG, "设备断开连接...");
                mOnConnectionListener.onDeviceDisconnected();
            }

            @Override
            public void onServicesDiscovered(BluetoothGatt gatt) {
                Log.i(TAG, "发现服务");
                mIOCharacteristic = gatt.getService(UUID.fromString(UUIDInfo.SERVICE_IO)).getCharacteristic(UUID.fromString(UUIDInfo.CHARACTERISTIC_IO));
                enableReceiveData(gatt, mIOCharacteristic);
                mIOGatt = gatt;
            }

            @Override
            public void onDeviceConnectFail(ConnBleException e) {
                Log.i(TAG, "设备连接失败");
                mOnConnectionListener.onDeviceConnectError(e.getDetailMessage());
            }
        });
        mBluetoothLe.setOnReadCharacteristicListener(new OnLeReadCharacteristicListener() {
            @Override
            public void onSuccess(BluetoothGattCharacteristic characteristic) {
                Log.i(TAG, "成功读取特征" + characteristic.getStringValue(0));
            }

            @Override
            public void onFailure(ReadBleException e) {
                Log.e(TAG, "读取特征失败");
            }
        });
        mBluetoothLe.setOnWriteCharacteristicListener(new OnLeWriteCharacteristicListener() {
            @Override
            public void onSuccess(BluetoothGattCharacteristic characteristic) {
                Log.i(TAG, "成功写入数据" + characteristic.getStringValue(0));
            }

            @Override
            public void onFailed(WriteBleException e) {
                Log.e(TAG, "写入数据失败");
            }
        });
        mBluetoothLe.setOnNotificationListener(new OnLeNotificationListener() {
            @Override
            public void onSuccess(BluetoothGattCharacteristic characteristic) {
                String realData = CJMCU.parserData(characteristic.getValue());
                Log.i(TAG, "onNotificationSuccess:" + realData);
                mOnConnectionListener.onDataReceived(realData);
            }

            @Override
            public void onFailed(BleException e) {
                Log.i(TAG, "onNotificationError:" + e.getDetailMessage());
            }
        });
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    private void enableReceiveData(BluetoothGatt gatt, BluetoothGattCharacteristic mIOCharacteristic) {
        gatt.setCharacteristicNotification(mIOCharacteristic, true);
    }

    @Override
    public void registerCallback(OnConnectionListener listener) {
        if (listener != null) {
            mOnConnectionListener = listener;
        }
    }

    @Override
    public void connectByWifi(String arg1, int arg2) {

    }

    @Override
    public void connectByBle(BluetoothDevice device, UUID serviceUuid, UUID characticUuid) {
        mBluetoothLe.startConnect(device);
    }

    @Override
    public void disConnect() {
        mBluetoothLe.disconnect();
    }

    @Override
    public void sendData(String data) {
        mIOCharacteristic.setValue(data.getBytes());
        mIOGatt.writeCharacteristic(mIOCharacteristic);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mBluetoothLe.destroy();
    }
}
