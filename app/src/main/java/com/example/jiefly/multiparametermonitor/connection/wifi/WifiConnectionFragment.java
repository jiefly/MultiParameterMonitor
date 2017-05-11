package com.example.jiefly.multiparametermonitor.connection.wifi;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.jiefly.multiparametermonitor.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class WifiConnectionFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "WifiConnectionFragment";
    private EditText mIpET;
    private EditText mPortET;
    private Button mConnectBtn;
    private Button mDisconnectBtn;
    private String mServerIp;
    private int mServerPort;
    private volatile boolean connected;
    private Socket socket;


    public WifiConnectionFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                connectServer();
                break;
            case R.id.id_wifi_disconnect:
                connected = false;
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
                    while (socket.isConnected() && connected && (line = br.readLine()) != null) {
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
}
