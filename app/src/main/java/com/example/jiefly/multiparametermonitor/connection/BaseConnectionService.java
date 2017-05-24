package com.example.jiefly.multiparametermonitor.connection;

import android.app.Service;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Created by chgao on 17-5-24.
 */

public abstract class BaseConnectionService extends Service implements Connection {
    public final String TAG = this.getClass().getSimpleName();
    protected Set<OnConnectionListener> mConnectionListeners;

    @Override
    public void onCreate() {
        super.onCreate();
        mConnectionListeners = new HashSet<>();
        ConnectionManager.getInstance().addConnection(this);
    }

    @Override
    public void registerCallback(OnConnectionListener listener) {
        if (listener != null) {
            if (!mConnectionListeners.add(listener)) {
                Log.w(TAG, "the connection listener is register,don't register again");
            }
        }
    }

    @Override
    public void releaseCallback(OnConnectionListener listener) {
        if (!mConnectionListeners.remove(listener)) {
            Log.w(TAG, "the connection listener did't exist");
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new ConnectionBinder(this);
    }

    @Override
    public void connectByBle(BluetoothDevice device, UUID service, UUID charactist) {
        if (ConnectionManager.getInstance().getConnection() != getConnection()) {
            ConnectionManager.getInstance().addConnection(getConnection());
        }
    }

    @Override
    public void connectByWifi(String arg1, int arg2) {
        if (ConnectionManager.getInstance().getConnection() != getConnection()) {
            ConnectionManager.getInstance().addConnection(getConnection());
        }
    }

    protected abstract Connection getConnection();

    @Override
    public void onDestroy() {
        super.onDestroy();
        ConnectionManager.getInstance().destroyConnection(this);
    }
}
