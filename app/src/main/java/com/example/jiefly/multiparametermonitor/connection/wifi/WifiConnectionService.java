package com.example.jiefly.multiparametermonitor.connection.wifi;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.jiefly.multiparametermonitor.connection.Connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by chgao on 17-5-12.
 */

public class WifiConnectionService extends Service {
    private static String TAG = "WifiConnectionService";
    private Socket mSocket;
    private Connection mConnection;
    private OutputStream mOutputStream;
    private AtomicBoolean mClientClose;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public WifiConnectionService registerCallback(Connection connection) {
        mConnection = connection;
        return this;
    }

    public void connect(final String ip, final int port) {
        if (mSocket != null && mSocket.isConnected()) {
            Log.e(TAG, "wifi is connected to server:" + mSocket.getRemoteSocketAddress().toString());
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mConnection.onDeviceConnecting();
                    mSocket = new Socket(ip, port);
                    mConnection.onDeviceConnected();
                    BufferedReader br = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
                    String line;
                    while (!mSocket.isClosed() && mSocket.isConnected() && (line = br.readLine()) != null) {
                        mConnection.onDataReceived(line);
                        sendData(line);
                        Log.i(TAG, "data from Server:" + line);
                    }
                    mConnection.onDeviceDisconnected();
                    br.close();
                    if (mSocket.isConnected()) {
                        mSocket.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    mConnection.onDeviceConnectError("connection error:" + e.getMessage());
                    Log.e(TAG, "connection error:" + e.getMessage());

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

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
