package com.example.jiefly.multiparametermonitor.measuring

import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.example.jiefly.multiparametermonitor.R
import com.example.jiefly.multiparametermonitor.measuring.adapter.HeartRateRvAdapter
import com.example.jiefly.multiparametermonitor.measuring.data.HeartRateData
import com.example.jiefly.multiparametermonitor.measuring.data.historydata.HeartRateHistoryData
import com.example.jiefly.multiparametermonitor.measuring.view.RingView
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import java.util.*

/**
 * Created by chgao on 17-5-21.
 */
class MeasureHeartRate : MeasureBaseFragment(), RingView.OnAnimationListener {
    var recycleView: RecyclerView? = null
    var ringView: RingView? = null
    var adapter: HeartRateRvAdapter? = null
    var bottomBar: LinearLayout? = null
    var bottomTextView: TextView? = null
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_measure_heart_rate, container, false)
        initView(view)
        return view
    }

    private fun initView(view: View) {
        ringView = view.findViewById(R.id.id_heart_beat) as RingView
        ringView?.animationListener = this
        bottomBar = view.findViewById(R.id.bottom_bar) as LinearLayout
        bottomTextView = bottomBar?.findViewById(R.id.id_bottom_bar_text) as TextView
        bottomBar?.setOnClickListener {
            val showing = ringView!!.mAnimationShowing
            if (showing) {
                ringView?.stopAnim()
            } else {
                ringView?.startAnim()
            }
        }
        recycleView = view.findViewById(R.id.id_heart_rate_rv) as RecyclerView
        initRv()
    }

    private fun initRv() {
        adapter = HeartRateRvAdapter(activity)
        recycleView?.layoutManager = LinearLayoutManager(activity)
        val decoration = DividerItemDecoration(activity, DividerItemDecoration.VERTICAL)
        decoration.setDrawable(resources.getDrawable(R.drawable.div_bg))
        recycleView?.addItemDecoration(decoration)
        recycleView?.adapter = adapter
        var data = HeartRateHistoryData()
        for (i in 1..5) {
            data = HeartRateHistoryData()
            data.date = Date(System.currentTimeMillis() - 60 * 1000 * Random().nextInt(10) - i * 10 * 60 * 1000)
            data.value = HeartRateData().setRate(74 + Random().nextInt(i))
            adapter?.addData(data)
        }
        adapter?.notifyDataSetChanged()
    }

    override fun onAnimationEnd() {
        Observable.just("开始").observeOn(AndroidSchedulers.mainThread()).subscribe { bottomTextView?.text = it }
    }

    override fun onAnimationStart() {
        Observable.just("停止").observeOn(AndroidSchedulers.mainThread()).subscribe { bottomTextView?.text = it }
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