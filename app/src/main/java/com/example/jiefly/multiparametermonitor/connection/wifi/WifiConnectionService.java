package com.example.jiefly.multiparametermonitor.connection.wifi;

import android.bluetooth.BluetoothDevice;
import android.util.Log;

import com.example.jiefly.multiparametermonitor.connection.BaseConnectionService;
import com.example.jiefly.multiparametermonitor.connection.Connection;
import com.example.jiefly.multiparametermonitor.connection.OnConnectionListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.UUID;

/**
 * Created by chgao on 17-5-12.
 */

public class WifiConnectionService extends BaseConnectionService {
    private static String TAG = "WifiConnectionService";
    private Socket mSocket;
    private OutputStream mOutputStream;

    @Override
    public void connectByWifi(final String ip, final int port) {
        super.connectByWifi(ip, port);
        if (mSocket != null && mSocket.isConnected()) {
            Log.e(TAG, "wifi is connected to server:" + mSocket.getRemoteSocketAddress().toString());
            return;
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
                        sendData(line);
                        Log.i(TAG, "data from Server:" + line);
                        for (OnConnectionListener listener : mConnectionListeners) {
                            listener.onDataReceived(line);
                        }
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
        Log.e(TAG, this.getClass().getSimpleName() + "did't implements connectByBle,plz invoke connectByWifi");
    }

    @Override
    protected Connection getConnection() {
        return this;
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
            for (OnConnectionListener listener : mConnectionListeners) {
                listener.onDeviceDisconnected();
            }
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
