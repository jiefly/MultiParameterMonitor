package com.example.jiefly.multiparametermonitor.application;

import android.app.Application;
import android.content.SharedPreferences;

import com.qindachang.bluetoothle.BluetoothConfig;
import com.qindachang.bluetoothle.BluetoothLe;

/**
 * Created by chgao on 17-5-8.
 */

public class MyApplication extends Application {
    private final static String TAG = "MyApplication";
    private static SharedPreferences mSharedPreferences;
    @Override
    public void onCreate() {
        super.onCreate();
        BluetoothConfig config = new BluetoothConfig.Builder()
                .enableQueueInterval(true)//开启队列定时
                .setQueueIntervalTime(150)//设置定时150ms时长（才会发下一条），单位ms
                .build();
        BluetoothLe.getDefault().init(this, config);
    }

    @Override
    public void registerActivityLifecycleCallbacks(ActivityLifecycleCallbacks callback) {
        super.registerActivityLifecycleCallbacks(callback);
    }

    @Override
    public SharedPreferences getSharedPreferences(String name, int mode) {
        if (mSharedPreferences == null) {
            mSharedPreferences = super.getSharedPreferences(TAG, MODE_PRIVATE);
        }
        return mSharedPreferences;
    }
}
