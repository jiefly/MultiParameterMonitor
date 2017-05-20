package com.example.jiefly.multiparametermonitor.measuring

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.jiefly.multiparametermonitor.R
import com.jonas.jgraph.graph.JcoolGraph
import com.jonas.jgraph.models.Jchart
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by chgao on 17-5-20.
 */
class MeasureBooldPresure : MeasureBaseFragment() {
    var jGraph: JcoolGraph? = null
    val chartDatas = ArrayList<Jchart>()

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_measure_blood_pressure, container, false)
        this.jGraph = view.findViewById(R.id.id_measure_boold_pressure_chart) as JcoolGraph
        jGraph?.let { initGraph(jGraph!!) }
        return view
    }

    private fun initGraph(jGraph: JcoolGraph) {

        var jchart: Jchart
        for (i in 1..10) {
            jchart = Jchart((Random().nextInt(50) + 15).toFloat(), Color.RED)
            jchart.mTopRound = true
            jchart.lower = jchart.upper / 2
            jchart.xmsg = "$i x"
            jchart.textMsg = "$i y"
            jchart.tag = "$i tag"
            jchart.lowStart = 20f
            jchart.showMsg = "$i th data is ${jchart.upper}"
            chartDatas.add(jchart)
        }

        //选中模式
        jGraph.setSelectedMode(1)
        //连接线的颜色
        jGraph.setPaintShaderColors(Color.RED, Color.parseColor("#E79D23"),
                Color.parseColor("#FFF03D"), Color.parseColor("#A9E16F"), Color.parseColor("#75B9EF"))
        jGraph.setYaxisValues("fuck", "you", "!!")
        //线下区域的颜色
        jGraph.setShaderAreaColors(Color.WHITE, Color.GRAY)

        jGraph.feedData(chartDatas)
        jGraph.setBackgroundColor(activity.resources.getColor(R.color.cardview_dark_background))
        //chart.setYaxisValues(20, 80, 3)
        jGraph.setLinePointRadio(jGraph.lineWidth.toInt())

        jGraph.isScrollAble = true
    }

    override fun onPerHandleData(s: String): String? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onHandleData(s: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}