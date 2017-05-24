package com.example.jiefly.multiparametermonitor.connection;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.util.Log;

import java.util.Arrays;

/**
 * Created by chgao on 17-5-24.
 */

public abstract class BaseConnectionFragment extends Fragment implements OnConnectionListener {
    protected Connection mConnection;
    protected ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mConnection = (Connection) service;
            mConnection.registerCallback(getConnectionListener());
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mConnection.releaseCallback(getConnectionListener());
        }
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        getActivity().startService(new Intent(getActivity(), getService()));
        getActivity().bindService(new Intent(getActivity(), getService()), mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    protected abstract Class<?> getService();

    @Override
    public void onDetach() {
        super.onDetach();
        getActivity().unbindService(mServiceConnection);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mConnection != null) {
            mConnection.releaseCallback(this);
        }
    }

    @Override
    public void onDeviceConnecting() {
        Log.i(getTAG(), "Device connecting");
    }

    @Override
    public void onDataReceived(String s) {
        Log.i(getTAG(), "receive data:" + s);
    }

    @Override
    public void onDataReceived(byte[] data) {
        Log.i(getTAG(), "receive data:" + Arrays.toString(data));
    }

    @Override
    public void onDeviceConnected() {
        Log.i(getTAG(), "Device connected");
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
    }

    @Override
    public void onDeviceDisconnected() {
        Log.i(getTAG(), " Device disconnected");
    }

    @Override
    public void onDeviceConnectError(String message) {
        Log.w(getTAG(), "Device connect error:" + message);
    }

    protected abstract String getTAG();

    protected abstract OnConnectionListener getConnectionListener();
}
