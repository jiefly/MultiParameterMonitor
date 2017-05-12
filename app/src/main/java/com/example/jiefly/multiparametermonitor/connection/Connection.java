package com.example.jiefly.multiparametermonitor.connection;

/**
 * Created by chgao on 17-5-12.
 */

public interface Connection<D> {
    void sendData(String s);

    void sendData(char[] data);

    void onDataReceived(String s);

    void onDeviceConnecting();

    void onDeviceConnected();

    void onDeviceDisconnected();

    void onDeviceConnectError(String message);
}
