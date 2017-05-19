package com.example.jiefly.multiparametermonitor.connection;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.jiefly.multiparametermonitor.main.MainActivity;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by chgao on 17-5-18.
 */

public class ConnectionManager {
    private static final String TAG = "ConnectionManager";
    private static ConnectionManager mInstance;
    private Connection mConnection;
    private Set<MainActivity.OnConnectedListener> onConnectedListeners;

    private ConnectionManager() {
        onConnectedListeners = new HashSet<>();
    }

    public static ConnectionManager getInstance() {
        if (mInstance == null) {
            synchronized (ConnectionManager.class) {
                if (mInstance == null) {
                    mInstance = new ConnectionManager();
                }
            }
        }
        return mInstance;
    }

    public void registerListener(final MainActivity.OnConnectedListener listener) {
        if (listener != null) {
            onConnectedListeners.add(listener);
        }
    }

    public void releaseListener(MainActivity.OnConnectedListener listener) {
        // TODO: 17-5-18 shoule remove callback in mConnection
        onConnectedListeners.remove(listener);
    }

    public void setupConnection(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, ConnectionActivity.class);
        context.startActivity(intent);
    }

    public void addConnection(Connection connection) {
        if (connection != null) {
            mConnection = connection;
            mConnection.registerCallback(new OnConnectionListener() {
                @Override
                public void sendData(String s) {

                }

                @Override
                public void sendData(char[] data) {

                }

                @Override
                public void onDataReceived(String s) {

                }

                @Override
                public void onDeviceConnecting() {

                }

                @Override
                public void onDeviceConnected() {
                    for (MainActivity.OnConnectedListener listener : onConnectedListeners) {
                        listener.onConnected();
                    }
                }

                @Override
                public void onDeviceDisconnected() {
                    for (MainActivity.OnConnectedListener listener : onConnectedListeners) {
                        listener.onDisconnected();
                    }
                }

                @Override
                public void onDeviceConnectError(String message) {

                }
            });
        }
    }

    public boolean isConnected() {
        return mConnection != null && mConnection.isConnected();
    }

    public void destroyConnection(Connection connection) {
        if (connection != null && connection.equals(mConnection)) {
            mConnection = null;
        } else {
            Log.w(TAG, "The mConnection which you want destroy is't the mConnection in ConnectionManager");
        }
    }

    public Connection.ConnectionType getConnectionType() {
        return mConnection == null ? Connection.ConnectionType.UNKONW : mConnection.getConnectionType();
    }
}
