package com.example.jiefly.multiparametermonitor.connection;

import android.bluetooth.BluetoothDevice;

import java.util.UUID;

/**
 * Created by chgao on 17-5-13.
 */

public interface Connection {
    void registerCallback(OnConnectionListener listener);

    void connectByWifi(String arg1, int arg2);

    void connectByBle(BluetoothDevice device, UUID service, UUID charactist);

    void disConnect();

    void sendData(String data) throws Exception;
}
