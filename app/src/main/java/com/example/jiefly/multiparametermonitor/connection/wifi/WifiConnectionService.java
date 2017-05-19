package com.example.jiefly.multiparametermonitor.connection.wifi;

import android.app.Service;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.jiefly.multiparametermonitor.connection.Connection;
import com.example.jiefly.multiparametermonitor.connection.ConnectionBinder;
import com.example.jiefly.multiparametermonitor.connection.ConnectionManager;
import com.example.jiefly.multiparametermonitor.connection.OnConnectionListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Created by chgao on 17-5-12.
 */

public class WifiConnectionService extends Service implements Connection {
    private static String TAG = "WifiConnectionService";
    private Socket mSocket;
    private OutputStream mOutputStream;
    private Set<OnConnectionListener> mConnectionListeners;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "WifiConnectionService onCreate");
        mConnectionListeners = new HashSet<>();
        ConnectionManager.getInstance().addConnection(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new ConnectionBinder(this);
    }

    public void registerCallback(OnConnectionListener onConnectionListener) {
        if (onConnectionListener != null) {
            mConnectionListeners.add(onConnectionListener);
        }
    }

    @Override
    public void releaseCallback(OnConnectionListener listener) {
        mConnectionListeners.remove(listener);
    }

    @Override
    public void connectByWifi(final String ip, final int port) {
        if (mSocket != null && mSocket.isConnected()) {
            Log.e(TAG, "wifi is connected to server:" + mSocket.getRemoteSocketAddress().toString());
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    for (OnConnectionListener listener : mConnectionListeners) {
                        listener.onDeviceConnecting();
                    }
                    mSocket = new Socket(ip, port);
                    for (OnConnectionListener listener : mConnectionListeners) {
                        listener.onDeviceConnected();
                    }
                    BufferedReader br = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
                    String line;
                    while (!mSocket.isClosed() && mSocket.isConnected() && (line = br.readLine()) != null) {
                        for (OnConnectionListener listener : mConnectionListeners) {
                            listener.onDataReceived(line);
                        }
                        sendData(line);
                        Log.i(TAG, "data from Server:" + line);
                    }
                    for (OnConnectionListener listener : mConnectionListeners) {
                        listener.onDeviceDisconnected();
                    }
                    br.close();
                    if (mSocket.isConnected()) {
                        mSocket.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    for (OnConnectionListener listener : mConnectionListeners) {
                        listener.onDeviceConnectError("connection error:" + e.getMessage());
                    }
                    Log.e(TAG, "connection error:" + e.getMessage());

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    @Override
    public void connectByBle(BluetoothDevice device, UUID service, UUID charactist) {

    }

    @Override
    public void disConnect() {
        if (mSocket != null && mSocket.isConnected()) {
            try {
                mSocket.close();
                if (mOutputStream != null) {
                    mOutputStream.close();
                    mOutputStream = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void sendData(String s) throws Exception {
        if (mSocket == null || !mSocket.isConnected()) {
            throw new Exception("Socket is null or socket did't connected");
        }
        if (mOutputStream == null) {
            mOutputStream = mSocket.getOutputStream();
        }
        mOutputStream.write(s.getBytes());
        mOutputStream.flush();
    }

    @Override
    public boolean isConnected() {
        return mSocket != null && mSocket.isConnected();
    }

    @Override
    public ConnectionType getConnectionType() {
        return ConnectionType.WIFI;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "WifiConnectionService onDestroy");
        ConnectionManager.getInstance().destroyConnection(this);
        try {
            if (mSocket != null && mSocket.isConnected()) {
                mSocket.close();
            }
            if (mOutputStream != null) {
                mOutputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
