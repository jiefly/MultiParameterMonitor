package com.example.jiefly.multiparametermonitor.connection;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.jiefly.multiparametermonitor.R;
import com.example.jiefly.multiparametermonitor.connection.ble.BleConnectionFragment;
import com.example.jiefly.multiparametermonitor.connection.wifi.WifiOnConnectionListenerFragment;

public class ConnectionActivity extends AppCompatActivity implements ChooseConnectionViewer {
    private static final String TAG = "ConnectionActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection);
        initToolBar();
        showChoose();
    }

    private void showChoose() {
        FragmentManager manager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.id_container, new ChooseConnectionFragment(), "Choose");
        transaction.commit();
    }

    private void initToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.id_normal_tool_bar);
        ((TextView) toolbar.findViewById(R.id.id_tool_bar_title)).setText("连接设备");
        toolbar.findViewById(R.id.id_tool_bar_left_arrow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "back arrow clicked");
                getSupportFragmentManager().popBackStack();
            }
        });
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
    }


    @Override
    public void connectionByBle() {
        FragmentManager manager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.id_container, new BleConnectionFragment(), "ble");
        transaction.addToBackStack("BleFragment");
        transaction.commit();
    }

    @Override
    public void connectionByWifi() {
        FragmentManager manager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.id_container, new WifiOnConnectionListenerFragment(), "wifi");
        transaction.addToBackStack("WifiFragment");
        transaction.commit();
    }

    @Override
    public void backToChoose() {

    }
}
