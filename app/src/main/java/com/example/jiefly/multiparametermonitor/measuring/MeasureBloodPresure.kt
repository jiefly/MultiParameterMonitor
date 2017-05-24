package com.example.jiefly.multiparametermonitor.measuring

import android.graphics.Color
import android.graphics.DashPathEffect
import android.os.Bundle
import android.support.v7.widget.CardView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import com.example.jiefly.multiparametermonitor.R
import com.example.jiefly.multiparametermonitor.measuring.data.BooldPressureData
import com.example.jiefly.multiparametermonitor.measuring.data.mock.MockBooldPressureData
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import java.util.*
import kotlin.collections.ArrayList


/**
 * Created by chgao on 17-5-20.
 */
class MeasureBloodPresure : MeasureBaseFragment() {
    override fun onDataReceived(data: ByteArray?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    var xDatas = ArrayList<String>()
    var lowDatas = ArrayList<Entry>()
    var highDatas = ArrayList<Entry>()
    var dataSet = ArrayList<ILineDataSet>()
    var mChart: LineChart? = null
    var measuring = false

    init {
        val set1 = LineDataSet(highDatas, "高压")
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
        val set2 = LineDataSet(lowDatas, "低压")
        set2.setDrawValues(false)
        set2.enableDashedLine(10f, 5f, 0f)
        set2.enableDashedHighlightLine(10f, 5f, 0f)
        set2.color = Color.GREEN
        set2.setCircleColor(Color.GREEN)
        set2.lineWidth = 1f
        set2.circleRadius = 3f
        set2.setDrawCircleHole(true)
        set2.valueTextSize = 9f
        set2.setDrawFilled(true)
        set2.formLineWidth = 1f
        set2.formLineDashEffect = DashPathEffect(floatArrayOf(10f, 5f), 0f)
        set2.formSize = 10f
        set2.fillAlpha = 0
        set2.mode = LineDataSet.Mode.CUBIC_BEZIER
        dataSet.add(set1)
        dataSet.add(set2)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_measure_blood_pressure, container, false)
        this.mChart = view.findViewById(R.id.id_measure_boold_pressure_chart) as LineChart
        mChart?.let { initGraph(mChart!!) }
        initView(view)
        mock()
        return view
    }

    private fun initView(view: View) {
        val startCard = view.findViewById(R.id.id_start_card) as CardView
        val startCardText = startCard.findViewById(R.id.id_start_text) as TextView
        val startCardProgressbar = startCard.findViewById(R.id.id_start_progress_bar) as ProgressBar
        startCard.setOnClickListener {
            if (!measuring) {
                startCardText.text = "停止"
                startCardProgressbar.visibility = View.VISIBLE
            } else {
                startCardText.text = "开始"
                startCardProgressbar.visibility = View.INVISIBLE
            }
            measuring = !measuring
        }
    }

    override fun mock() {
        super.mock()
        var data: BooldPressureData
        var i = 0
        Timer().schedule(object : TimerTask() {
            override fun run() {
                while (!measuring) {
                    Thread.sleep(100)
                }
                data = MockBooldPressureData().mock()
                lowDatas.add(Entry(i.toFloat(), data.low.toFloat()))
                highDatas.add(Entry(i.toFloat(), data.high.toFloat()))
                xDatas.add(i.toString())
                (mChart?.data?.getDataSetByIndex(0) as LineDataSet).values = lowDatas
                (mChart?.data?.getDataSetByIndex(1) as LineDataSet).values = highDatas
                mChart?.data?.notifyDataChanged()
                mChart?.notifyDataSetChanged()
                mChart?.postInvalidate()
                i++
            }
        }, 0, 3000)

    }

    private fun setData(count: Int, range: Float) {
        val values = ArrayList<Entry>()
        val chart = this.mChart as LineChart
        for (i in 0..count - 1) {

            val `val` = (Math.random() * range).toFloat() + 3
            values.add(Entry(i.toFloat(), `val`, resources.getDrawable(R.drawable.arrow_left_default)))
        }

        val set1: LineDataSet

        if (chart.getData() != null && chart.getData().getDataSetCount() > 0) {
            set1 = chart.getData().getDataSetByIndex(0) as LineDataSet
            set1.values = values
            chart.getData().notifyDataChanged()
            chart.notifyDataSetChanged()
        } else {
            // create a dataset and give it a type
            set1 = LineDataSet(values, "DataSet 1")


            // set the line to be drawn like this "- - - - - -"
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
            set1.formSize = 15f

            set1.fillColor = Color.BLACK


            val dataSets = ArrayList<ILineDataSet>()
            dataSets.add(set1) // add the datasets

            // create a data object with the datasets
            val data = LineData(dataSets)

            // set data
            chart.setData(data)
        }
    }

    private fun initGraph(chart: LineChart) {
        chart.description.text = "血压数据"
        chart.setScaleEnabled(true)
        chart.data = LineData(dataSet)
        chart.data.notifyDataChanged()
        chart.notifyDataSetChanged()
    }

    override fun onPerHandleData(s: String): String? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onHandleData(s: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}