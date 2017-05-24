package com.example.jiefly.multiparametermonitor.connection.ble;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.util.Log;

import com.example.jiefly.multiparametermonitor.connection.BaseConnectionService;
import com.example.jiefly.multiparametermonitor.connection.Connection;
import com.example.jiefly.multiparametermonitor.connection.OnConnectionListener;
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

public class BleConnectionService extends BaseConnectionService {
    private static final String TAG = "BleConnectionService";
    private BluetoothLe mBluetoothLe;
    private BluetoothGatt mIOGatt;
    private BluetoothGattCharacteristic mIOCharacteristic;

    public BleConnectionService() {
        mBluetoothLe = BluetoothLe.getDefault();
        mBluetoothLe.setOnConnectListener(new OnLeConnectListener() {
            @Override
            public void onDeviceConnecting() {
                Log.i(TAG, "设备连接中...");
                for (OnConnectionListener listener : mConnectionListeners) {
                    listener.onDeviceConnecting();
                }
            }

            @Override
            public void onDeviceConnected() {
                Log.i(TAG, "设备连接成功");

            }

            @Override
            public void onDeviceDisconnected() {
                Log.i(TAG, "设备断开连接...");
                for (OnConnectionListener listener : mConnectionListeners) {
                    listener.onDeviceDisconnected();
                }
            }

            @Override
            public void onServicesDiscovered(BluetoothGatt gatt) {
                Log.i(TAG, "发现服务");
                mIOCharacteristic = gatt.getService(UUID.fromString(UUIDInfo.SERVICE_IO)).getCharacteristic(UUID.fromString(UUIDInfo.CHARACTERISTIC_IO));
                enableReceiveData(gatt, mIOCharacteristic);
                mIOGatt = gatt;
                for (OnConnectionListener listener : mConnectionListeners) {
                    listener.onDeviceConnected();
                }
            }

            @Override
            public void onDeviceConnectFail(ConnBleException e) {
                Log.i(TAG, "设备连接失败");
                for (OnConnectionListener listener : mConnectionListeners) {
                    listener.onDeviceConnectError(e.getDetailMessage());
                }
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
                //String realData = CJMCU.parserData(characteristic.getValue());
                String realData = characteristic.getStringValue(0);
                Log.i(TAG, "onNotificationSuccess:" + realData);
                for (OnConnectionListener listener : mConnectionListeners) {
                    listener.onDataReceived(characteristic.getValue());
                }
            }

            @Override
            public void onFailed(BleException e) {
                Log.i(TAG, "onNotificationError:" + e.getDetailMessage());
            }
        });

    }

    private void enableReceiveData(BluetoothGatt gatt, BluetoothGattCharacteristic mIOCharacteristic) {
        gatt.setCharacteristicNotification(mIOCharacteristic, true);
    }

    @Override
    public void connectByWifi(String arg1, int arg2) {
        Log.e(TAG, this.getClass().getSimpleName() + "did't implements connectByWifi ,plz invoke connectByBle");
    }

    @Override
    public void connectByBle(BluetoothDevice device, UUID serviceUuid, UUID characticUuid) {
        super.connectByBle(device, serviceUuid, characticUuid);
        mBluetoothLe.startConnect(device);
    }

    @Override
    protected Connection getConnection() {
        return this;
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
    public boolean isConnected() {
        return mBluetoothLe.getConnected();
    }

    @Override
    public ConnectionType getConnectionType() {
        return ConnectionType.BLE;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mBluetoothLe.destroy();
    }


}
