package com.example.jiefly.multiparametermonitor.connection.wifi;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.jiefly.multiparametermonitor.R;
import com.example.jiefly.multiparametermonitor.connection.BaseConnectionFragment;
import com.example.jiefly.multiparametermonitor.connection.OnConnectionListener;

public class WifiOnConnectionListenerFragment extends BaseConnectionFragment implements View.OnClickListener {
    private static final String TAG = "WifiOnConnectionListenerFragment";
    private EditText mIpET;
    private EditText mPortET;
    private Button mConnectBtn;
    private Button mDisconnectBtn;
    private String mServerIp;
    private int mServerPort;

    public WifiOnConnectionListenerFragment() {
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
                mConnection.connectByWifi(mServerIp, mServerPort);
                break;
            case R.id.id_wifi_disconnect:
                mConnection.disConnect();
                break;
        }
    }

    @Override
    protected Class<?> getService() {
        return WifiConnectionService.class;
    }

    @Override
    protected String getTAG() {
        return TAG;
    }

    @Override
    protected OnConnectionListener getConnectionListener() {
        return WifiOnConnectionListenerFragment.this;
    }
}
