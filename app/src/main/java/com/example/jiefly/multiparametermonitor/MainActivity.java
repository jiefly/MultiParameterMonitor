package com.example.jiefly.multiparametermonitor;

import android.bluetooth.BluetoothDevice;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.qindachang.bluetoothle.BluetoothLe;
import com.qindachang.bluetoothle.OnLeScanListener;
import com.qindachang.bluetoothle.exception.ScanBleException;
import com.qindachang.bluetoothle.scanner.ScanRecord;
import com.qindachang.bluetoothle.scanner.ScanResult;

import java.util.List;

public class MainActivity extends AppCompatActivity{
    private static final String TAG = "MainActivity";
    private BluetoothLe mBluetoothLe;
    private BluetoothDevice mBluetoothDevice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.normal_item);
        mBluetoothLe = BluetoothLe.getDefault();
        if (!mBluetoothLe.isSupportBluetooth()) {
            //设备不支持蓝牙
            Toast.makeText(this,"device not support ble",Toast.LENGTH_SHORT).show();
        } else {
            if (!mBluetoothLe.isBluetoothOpen()) {
                //没有打开蓝牙，请求打开手机蓝牙
                mBluetoothLe.enableBluetooth(this, 666);
            }
        }
        //监听蓝牙回调
        //监听扫描
        mBluetoothLe.setOnScanListener(TAG, new OnLeScanListener() {
            @RequiresApi(api = Build.VERSION_CODES.ECLAIR)
            @Override
            public void onScanResult(BluetoothDevice bluetoothDevice, int rssi, ScanRecord scanRecord) {
                mBluetoothDevice = bluetoothDevice;
                Log.i(TAG, "扫描到设备：" + mBluetoothDevice.getName());
            }

            @Override
            public void onBatchScanResults(List<ScanResult> results) {
                Log.i(TAG, "扫描到设备：" + results.toString());
            }

            @Override
            public void onScanCompleted() {
                Log.i(TAG, "停止扫描");
            }

            @Override
            public void onScanFailed(ScanBleException e) {
                Log.e(TAG, "扫描错误：" + e.toString());
            }
        });
        scan();
    }
    private void scan() {
        mBluetoothLe.setScanPeriod(20000)
                //   .setScanWithServiceUUID(BluetoothUUID.SERVICE)
                .setReportDelay(0)
                .startScan(this);
    }
    @Override
    protected void onResume() {
        super.onResume();

    }
}
