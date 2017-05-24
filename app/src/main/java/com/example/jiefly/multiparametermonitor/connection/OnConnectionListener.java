package com.example.jiefly.multiparametermonitor.connection;

/**
 * Created by chgao on 17-5-12.
 */

public interface OnConnectionListener<D> {

    void onDataReceived(String s);

    void onDataReceived(byte[] data);

    void onDeviceConnecting();

    void onDeviceConnected();

    void onDeviceDisconnected();

    void onDeviceConnectError(String message);
}
