package com.example.jiefly.multiparametermonitor.application;

import android.app.Application;

import com.qindachang.bluetoothle.BluetoothConfig;
import com.qindachang.bluetoothle.BluetoothLe;

/**
 * Created by chgao on 17-5-8.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        BluetoothConfig config = new BluetoothConfig.Builder()
                .enableQueueInterval(true)//开启队列定时
                .setQueueIntervalTime(150)//设置定时150ms时长（才会发下一条），单位ms
                .build();
        BluetoothLe.getDefault().init(this, config);
    }
}
