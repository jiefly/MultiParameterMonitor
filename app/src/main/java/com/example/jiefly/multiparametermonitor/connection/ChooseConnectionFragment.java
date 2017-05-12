package com.example.jiefly.multiparametermonitor.connection;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jiefly.multiparametermonitor.R;

/**
 * Created by chgao on 17-5-11.
 */

public class ChooseConnectionFragment extends Fragment {
    private CardView mWifiConnection;
    private CardView mBleConnection;
    private ChooseConnectionViewer mViewer;
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewer = (ChooseConnectionViewer) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.choose_connection, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mWifiConnection = (CardView) view.findViewById(R.id.id_connection_wifi);
        mBleConnection = (CardView) view.findViewById(R.id.id_connection_ble);
        mWifiConnection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewer.connectionByWifi();
            }
        });
        mBleConnection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewer.connectionByBle();
            }
        });
    }
}
