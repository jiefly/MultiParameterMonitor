package com.example.jiefly.multiparametermonitor.measuring;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.example.jiefly.multiparametermonitor.R;

public class MeasureMain extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measure_main);
        FragmentManager manager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.id_container, new MeasureBooldPresure(), "MeasureEcg");
        transaction.commit();
    }
}
