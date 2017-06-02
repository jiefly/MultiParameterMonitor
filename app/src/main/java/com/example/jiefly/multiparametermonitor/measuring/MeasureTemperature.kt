package com.example.jiefly.multiparametermonitor.measuring

import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.example.jiefly.multiparametermonitor.R
import com.example.jiefly.multiparametermonitor.measuring.adapter.TemperatureRvAdapter
import com.example.jiefly.multiparametermonitor.measuring.data.TempetureData
import com.example.jiefly.multiparametermonitor.measuring.data.historydata.TemperatureHistoryData
import com.example.jiefly.multiparametermonitor.measuring.view.TemperatureView
import java.util.*

/**
 * Created by chgao on 17-5-20.
 */
class MeasureTemperature : MeasureBaseFragment() {
    var temView: TemperatureView? = null
    var recycleView: RecyclerView? = null
    var adapter: TemperatureRvAdapter? = null
    var bottomBar: LinearLayout? = null
    var bottomTextView: TextView? = null
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater?.inflate(R.layout.fragment_measure_temperature, container, false)
        initView(view!!)

        return view
    }

    private fun initView(view: View) {
        temView = view.findViewById(R.id.id_tem) as TemperatureView?
        temView?.currentTemp = 36.7f
        bottomBar = view.findViewById(R.id.bottom_bar) as LinearLayout
        (bottomBar?.findViewById(R.id.id_measure_bottom_bar_icon) as ImageView).setImageResource(R.drawable.ic_temputure)
        bottomTextView = bottomBar?.findViewById(R.id.id_bottom_bar_text) as TextView
        recycleView = view.findViewById(R.id.id_tem_rv) as RecyclerView
        initRv()
    }

    private fun initRv() {
        adapter = TemperatureRvAdapter(activity)
        recycleView?.layoutManager = LinearLayoutManager(activity)
        val decoration = DividerItemDecoration(activity, DividerItemDecoration.VERTICAL)
        decoration.setDrawable(resources.getDrawable(R.drawable.div_bg))
        recycleView?.addItemDecoration(decoration)
        recycleView?.adapter = adapter
        var data = TemperatureHistoryData()
        for (i in 1..10) {
            data.date = Date()
            data.value = TempetureData()
            adapter?.addData(data)
        }
        adapter?.notifyDataSetChanged()
    }

    override fun onDataReceived(data: ByteArray?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onHandleData(s: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onPerHandleData(s: String): String? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}