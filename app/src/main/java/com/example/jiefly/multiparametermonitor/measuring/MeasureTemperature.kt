package com.example.jiefly.multiparametermonitor.measuring

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.jiefly.multiparametermonitor.R

/**
 * Created by chgao on 17-5-20.
 */
class MeasureTemperature : Fragment() {
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_measure_blood_pressure, container, false)
    }
}