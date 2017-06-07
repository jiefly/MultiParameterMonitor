package com.example.jiefly.multiparametermonitor.record.fragment

import android.graphics.Color
import android.graphics.DashPathEffect
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
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
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
        initLineDatas()
        val setCollection = ArrayList<ILineDataSet>()
        for ((key, value) in dataCollection) {
            setCollection.add(initLine(value, key))
        }
        lineChart!!.description.text = getChartDes()
        lineChart?.data = LineData(setCollection)
        lineChart?.data?.notifyDataChanged()
        lineChart?.notifyDataSetChanged()
    }

    private fun initLineDatas() {

    }

    private fun initRv() {
        recycleView!!.layoutManager = LinearLayoutManager(activity) as RecyclerView.LayoutManager?
        val decoration = DividerItemDecoration(activity, DividerItemDecoration.VERTICAL)
        decoration.setDrawable(resources.getDrawable(R.drawable.div_bg))
        recycleView!!.addItemDecoration(decoration)
        adapter = BaseRvAdapter(activity)
        recycleView!!.adapter = adapter
        val chartData = ArrayList<Entry>()
        var data = NormalHistoryData<BaseMeasureData>()
        for (i in 1..4) {
            data = NormalHistoryData<BaseMeasureData>()
            data.date = Date(System.currentTimeMillis() - 60 * 1000 * Random().nextInt(10) - i * 10 * 60 * 1000)
            data.value = BaseMeasureData()
            data.value?.unit = "%"
            data.value?.value = "${98 + Random().nextInt(2) - 1}"
            chartData.add(Entry(i.toFloat(), data.value?.value!!.toFloat()))
            adapter?.addData(data)
        }
        dataCollection.put("血氧", chartData)
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

    open fun initLine(datas: ArrayList<Entry>, title: String): LineDataSet {
        val set1 = LineDataSet(datas, title)
        set1.setDrawValues(false)
        set1.enableDashedLine(10f, 5f, 0f)
        set1.enableDashedHighlightLine(10f, 5f, 0f)
        set1.color = Color.BLACK
        set1.setCircleColor(Color.BLACK)
        set1.lineWidth = 1f
        set1.circleRadius = 3f
        set1.setDrawCircleHole(true)
        set1.valueTextSize = 9f
        set1.setDrawFilled(true)
        set1.formLineWidth = 1f
        set1.formLineDashEffect = DashPathEffect(floatArrayOf(10f, 5f), 0f)
        set1.formSize = 10f
        set1.fillAlpha = 0
        set1.mode = LineDataSet.Mode.CUBIC_BEZIER
        return set1
    }

    open fun getChartDes(): String {
        return "血氧数据"
    }

}