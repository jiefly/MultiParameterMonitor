package com.example.jiefly.multiparametermonitor.record.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.jiefly.multiparametermonitor.R
import com.example.jiefly.multiparametermonitor.measuring.adapter.BaseRvAdapter
import com.example.jiefly.multiparametermonitor.measuring.data.BaseMeasureData
import com.example.jiefly.multiparametermonitor.measuring.data.historydata.NormalHistoryData
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

/**
 * Created by chgao on 17-6-3.
 */
open class BaseRecordItemFragment : Fragment() {
    var recycleView: RecyclerView? = null
    var lineChart: LineChart? = null
    var adapter: BaseRvAdapter? = null
    var title: TextView? = null
    val dataCollection = HashMap<String, ArrayList<Entry>>()
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(getLayoutRes(), container, false)
        initView(view)
        return view
    }

    private fun initView(view: View?) {
        recycleView = view!!.findViewById(R.id.id_normal_history_rv) as RecyclerView
        lineChart = view.findViewById(R.id.id_history_chart) as LineChart
        initRv()
        initChart()
    }

    private fun initChart() {
        val setCollection = ArrayList<ILineDataSet>()
        for (i in 1..lineNum()) {

        }
    }

    private fun initRv() {
        recycleView!!.layoutManager = LinearLayoutManager(activity) as RecyclerView.LayoutManager?
        val decoration = DividerItemDecoration(activity, DividerItemDecoration.VERTICAL)
        decoration.setDrawable(resources.getDrawable(R.drawable.div_bg))
        recycleView!!.addItemDecoration(decoration)
        adapter = BaseRvAdapter(activity)
        recycleView!!.adapter = adapter
        var data = NormalHistoryData<BaseMeasureData>()
        for (i in 1..10) {
            data.date = Date()
            data.value = BaseMeasureData()
            data.value?.value = "${100 + i}"
            adapter?.addData(data)
        }
        adapter?.notifyDataSetChanged()

    }

    protected fun getLayoutRes(): Int {
        return R.layout.fragment_base_record_item
    }


    fun setTitle(title: String) {
        this.title!!.text = title
    }

    open fun lineNum(): Int {
        return 1
    }

}