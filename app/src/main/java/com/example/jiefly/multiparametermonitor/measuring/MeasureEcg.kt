package com.example.jiefly.multiparametermonitor.measuring

import android.os.Bundle
import android.support.v7.widget.CardView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.jiefly.multiparametermonitor.R
import com.example.jiefly.multiparametermonitor.measuring.data.EcgData
import com.example.jiefly.multiparametermonitor.measuring.parser.BMD101
import com.example.jiefly.multiparametermonitor.measuring.parser.ParserHelper
import com.example.jiefly.multiparametermonitor.measuring.view.EcgView
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue

/**
 * Created by chgao on 17-5-20.
 */
class MeasureEcg : MeasureBaseFragment() {
    private val DATA_GET_FREQUENCY = 16L
    private val dataPool: Queue<Int>
    private val timer: Timer = Timer()
    var analyse: CardView? = null
    var start: CardView? = null

    init {
        dataPool = ConcurrentLinkedQueue<Int>()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mock()
        val view = inflater!!.inflate(R.layout.fragment_measure_ecg, container, false)
        initView(view)
        return view
    }

    private fun initView(view: View) {
        start = view.findViewById(R.id.id_measure_ecg_start_measure) as CardView
        analyse = view.findViewById(R.id.id_measure_ecg_any) as CardView
        analyse!!.setOnClickListener {
            showAlertDialog("分析结果", "您当前的心率正常", null, null)
        }
    }

    private fun simulator() {
        val data0Q = ParserHelper().parserEcg(kotlin.ByteArray(0x11), true).rawEcgValues
        Timer().schedule(object : TimerTask() {
            override fun run() {
                if (EcgView.isRunning) {
                    if (!data0Q.isEmpty()) {
                        EcgView.addEcgData0(data0Q.peek())
                        data0Q.add(data0Q.poll())
                    }
                }
            }
        }, 0, 2)
    }

    override fun onHandleData(s: String) {
        s.split(",").filter { it.isNotEmpty() }.forEach { dataPool.offer(it.toInt()) }
    }

    override fun onHandleData(data: ByteArray?) {
        super.onHandleData(data)
        data?.let { onPerHandleData(data) }
                ?.filter { it.realValue > -4096 && it.realValue < 4096 }
                ?.forEach { dataPool.offer(it.realValue.toInt()) }
    }

    override fun onPerHandleData(s: String): String? {
        return s.filter { it in '0'..'9' || it == ',' }
    }

    override fun onPerHandleData(data: ByteArray): List<EcgData> {
        super.onPerHandleData(data)
        return BMD101().parserEcgData(data)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showDataAuto()
    }

    fun stopShowData(): Unit {
        timer.cancel()
    }

    fun showDataAuto(): Unit {
        timer.schedule(object : TimerTask() {
            override fun run() {
                while (EcgView.isRunning && dataPool.isNotEmpty()) {
                    EcgView.addEcgData0(dataPool.poll())
                }
            }
        }, 0, DATA_GET_FREQUENCY)
    }

    override fun mock() {
        simulator()
    }
}