package com.example.jiefly.multiparametermonitor.connection.wifi;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.jiefly.multiparametermonitor.R;
import com.example.jiefly.multiparametermonitor.connection.Connection;
import com.example.jiefly.multiparametermonitor.connection.OnConnectionListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

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
    private volatile boolean connected;
    private Socket socket;
    private Connection mService;


    public WifiOnConnectionListenerFragment() {

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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_wifi_connect:
                mServerIp = mIpET.getEditableText().toString();
                mServerPort = Integer.valueOf(mPortET.getEditableText().toString());
                mService = new WifiConnectionService();
                mService.registerCallback(this);
                mService.connectByWifi(mServerIp, mServerPort);
//                connectServer();
                break;
            case R.id.id_wifi_disconnect:
                mService.disConnect();
                break;
        }
    }

    private void connectServer() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    socket = new Socket(mServerIp, mServerPort);
                    connected = true;
                    BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String line = "";
                    Message message;
                    Bundle bundle = new Bundle();
                    while (socket.isConnected() && connected && (line = br.readLine()) != null) {
                        message = Message.obtain();
                        bundle.clear();
                        bundle.putString("data", line);
                        message.setData(bundle);
                        mHandler.sendMessage(message);
                        Log.i(TAG, "data from Server:" + line);
                    }
                    br.close();
                    socket.close();
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage());
                    e.printStackTrace();
                }
            }
        }).start();
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
