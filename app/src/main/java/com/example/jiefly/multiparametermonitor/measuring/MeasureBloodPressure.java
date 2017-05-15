package com.example.jiefly.multiparametermonitor.measuring;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jiefly.multiparametermonitor.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MeasureBloodPressure extends Fragment {


    public MeasureBloodPressure() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_measure_blood_pressure, container, false);
    }

}
