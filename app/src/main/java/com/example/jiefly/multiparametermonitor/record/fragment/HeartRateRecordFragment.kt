package com.example.jiefly.multiparametermonitor.record.fragment

import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.jiefly.multiparametermonitor.R
import com.example.jiefly.multiparametermonitor.measuring.adapter.HeartRateRvAdapter
import com.example.jiefly.multiparametermonitor.measuring.data.HeartRateData
import com.example.jiefly.multiparametermonitor.measuring.data.historydata.HeartRateHistoryData
import java.util.*

/**
 * Created by chgao on 17-6-3.
 */
class HeartRateRecordFragment : BaseRecordItemFragment() {
    var recycleView1: RecyclerView? = null
    var adapter1: HeartRateRvAdapter? = null
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_heart_reate_record_item, container, false)

        recycleView1 = view.findViewById(R.id.id_record_blood_pressure_rv) as RecyclerView
        initRv()
        return view
    }

    private fun initRv() {
        adapter1 = HeartRateRvAdapter(activity)
        recycleView1?.layoutManager = LinearLayoutManager(activity)
        val decoration = DividerItemDecoration(activity, DividerItemDecoration.VERTICAL)
        decoration.setDrawable(resources.getDrawable(R.drawable.div_bg))
        recycleView1?.addItemDecoration(decoration)
        recycleView1?.adapter = adapter1
        var data = HeartRateHistoryData()
        for (i in 1..10) {
            data.date = Date()
            data.value = HeartRateData().setRate(100 + i)
            adapter1?.addData(data)
        }
        adapter1?.notifyDataSetChanged()
    }
}