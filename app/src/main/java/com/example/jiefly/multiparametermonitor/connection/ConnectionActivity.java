package com.example.jiefly.multiparametermonitor.connection;

import android.Manifest;
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

import pub.devrel.easypermissions.EasyPermissions;

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
                onBackPressed();
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

    private static final String[] locations = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

    public boolean checkLocationPermission() {
        return EasyPermissions.hasPermissions(this, locations);
    }

    public void requestLocationPermission() {
        EasyPermissions.requestPermissions(this, "Android 6.0以上扫描蓝牙需要该权限", Permission.LOCATION, locations);
    }
}
