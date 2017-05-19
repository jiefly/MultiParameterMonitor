package com.example.jiefly.multiparametermonitor.connection.wifi;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.jiefly.multiparametermonitor.R;
import com.example.jiefly.multiparametermonitor.connection.Connection;
import com.example.jiefly.multiparametermonitor.connection.OnConnectionListener;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class WifiOnConnectionListenerFragment extends Fragment implements View.OnClickListener, OnConnectionListener {
    private static final String TAG = "WifiOnConnectionListenerFragment";
    private static Handler mHandler;
    private EditText mIpET;
    private EditText mPortET;
    private Button mConnectBtn;
    private Button mDisconnectBtn;
    private String mServerIp;
    private int mServerPort;
    private Connection mConnection;
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mConnection = (Connection) service;
            mConnection.registerCallback(WifiOnConnectionListenerFragment.this);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mConnection.releaseCallback(WifiOnConnectionListenerFragment.this);
        }
    };

    public WifiOnConnectionListenerFragment() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        getActivity().startService(new Intent(getActivity(), WifiConnectionService.class));
        getActivity().bindService(new Intent(getActivity(), WifiConnectionService.class), mServiceConnection, Context.BIND_AUTO_CREATE);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Toast.makeText(getActivity(), msg.getData().getString("data"), Toast.LENGTH_SHORT).show();
            }
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_wifi_connection, container, false);
        mIpET = (EditText) v.findViewById(R.id.id_wifi_server_ip);
        mPortET = (EditText) v.findViewById(R.id.id_wifi_server_port);
        mConnectBtn = (Button) v.findViewById(R.id.id_wifi_connect);
        mDisconnectBtn = (Button) v.findViewById(R.id.id_wifi_disconnect);
        mConnectBtn.setOnClickListener(this);
        mDisconnectBtn.setOnClickListener(this);
        return v;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        getActivity().unbindService(mServiceConnection);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_wifi_connect:
                mServerIp = mIpET.getEditableText().toString();
                mServerPort = Integer.valueOf(mPortET.getEditableText().toString());
                mConnection.connectByWifi(mServerIp, mServerPort);
                break;
            case R.id.id_wifi_disconnect:
                mConnection.disConnect();
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mConnection != null) {
            mConnection.releaseCallback(this);
        }
    }

    @Override
    public void sendData(String s) {

    }

    @Override
    public void sendData(char[] data) {

    }

    @Override
    public void onDataReceived(String s) {
        Observable.just(s).subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        Toast.makeText(getActivity(), "receive data from server:" + s, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onDeviceConnecting() {

    }

    @Override
    public void onDeviceConnected() {
        Observable.just("onDeviceConnected").subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
                    }
                });
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
    }

    @Override
    public void onDeviceDisconnected() {
        Observable.just("onDeviceDisconnected").subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onDeviceConnectError(String message) {
        Observable.just(message).subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
