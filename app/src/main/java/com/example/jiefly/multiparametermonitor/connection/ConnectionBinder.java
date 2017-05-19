package com.example.jiefly.multiparametermonitor.connection;

import android.bluetooth.BluetoothDevice;
import android.os.Binder;

import java.util.UUID;

/**
 * Created by chgao on 17-5-18.
 */

public class ConnectionBinder extends Binder implements Connection {
    Connection mConnection;

    public ConnectionBinder(Connection connection) {
        this.mConnection = connection;
    }

    @Override
    public void registerCallback(OnConnectionListener listener) {
        mConnection.registerCallback(listener);
    }

    @Override
    public void releaseCallback(OnConnectionListener listener) {
        mConnection.releaseCallback(listener);
    }

    @Override
    public void connectByWifi(String arg1, int arg2) {
        mConnection.connectByWifi(arg1, arg2);
    }

    @Override
    public void connectByBle(BluetoothDevice device, UUID service, UUID charactist) {
        mConnection.connectByBle(device, service, charactist);
    }

    @Override
    public void disConnect() {
        mConnection.disConnect();
    }

    @Override
    public void sendData(String data) throws Exception {
        mConnection.sendData(data);
    }

    @Override
    public boolean isConnected() {
        return mConnection.isConnected();
    }

    @Override
    public ConnectionType getConnectionType() {
        return mConnection.getConnectionType();
    }
}
