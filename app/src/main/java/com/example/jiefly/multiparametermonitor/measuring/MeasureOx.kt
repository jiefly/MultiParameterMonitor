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
import com.example.jiefly.multiparametermonitor.measuring.adapter.BaseRvAdapter
import com.example.jiefly.multiparametermonitor.measuring.data.OxData
import com.example.jiefly.multiparametermonitor.measuring.data.historydata.NormalHistoryData
import com.mikhaellopez.circularprogressbar.CircularProgressBar
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.functions.Func1
import java.util.*

/**
 * Created by chgao on 17-5-21.
 */
class MeasureOx : MeasureBaseFragment() {
    companion object {
        @JvmField
        val MEASURE_TIME = 10
    }

    var recycleView: RecyclerView? = null
    var adapter: BaseRvAdapter? = null
    var progress: CircularProgressBar? = null
    var progressText: TextView? = null
    var timer: Timer? = null
    var progressIndex = 0
    var bottomBar: LinearLayout? = null
    var bottomTextView: TextView? = null
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_measure_ox, container, false)
        initView(view)
        return view
    }

    private fun initTimer() {
        timer = Timer()
        timer!!.schedule(object : TimerTask() {
            override fun run() {
                setProgressText(progressIndex.toString())
                Observable.just(progressIndex).map(Func1<Int, Float> {
                    return@Func1 it.toFloat()
                }).observeOn(AndroidSchedulers.mainThread()).subscribe {
                    progress!!.setProgressWithAnimation(progressIndex.toFloat(), 100)
                    progressIndex++
                    if (progressIndex == 101) {
                        setBottomBarText("开始")
                        adapter!!.addData(NormalHistoryData<OxData>())
                        adapter!!.notifyItemInserted(0)
                        cancel()
                    }
                }

            }
        }, 10, 10 * MEASURE_TIME.toLong())
    }

    private fun setProgressText(value: String) {
        Observable.just(value).subscribeOn(AndroidSchedulers.mainThread()).subscribe {
            progressText?.text = "$it %"
        }
    }

    private fun setBottomBarText(value: String) {
        Observable.just(value).observeOn(AndroidSchedulers.mainThread()).subscribe {
            bottomTextView?.text = it
        }
    }

    private fun initView(view: View) {
        bottomBar = view.findViewById(R.id.bottom_bar) as LinearLayout
        bottomTextView = bottomBar?.findViewById(R.id.id_bottom_bar_text) as TextView
        bottomBar?.setOnClickListener {

            if (progressIndex == 0) {
                setBottomBarText("停止")
                timer?.cancel()
                initTimer()
            } else {
                setBottomBarText("开始")
                progressIndex = 0
                timer?.cancel()
            }
        }
        recycleView = view.findViewById(R.id.id_ox_rv) as RecyclerView
        initRV()
        progress = view.findViewById(R.id.id_measure_ox_progress) as CircularProgressBar?
        progress!!.setOnClickListener {
            timer?.cancel()
            progressIndex = 0
            initTimer()
        }
        progressText = view.findViewById(R.id.id_measure_ox_progress_text) as TextView

    }

    private fun initRV() {
        adapter = BaseRvAdapter(activity)
        recycleView!!.layoutManager = LinearLayoutManager(activity)
        val decoration = DividerItemDecoration(activity, DividerItemDecoration.VERTICAL)
        decoration.setDrawable(resources.getDrawable(R.drawable.div_bg))
        recycleView?.addItemDecoration(decoration)
        recycleView!!.adapter = adapter
        var data = NormalHistoryData<OxData>()
        for (i in 1..4) {
            data = NormalHistoryData<OxData>()
            data.date = Date(System.currentTimeMillis() - 60 * 1000 * Random().nextInt(10) - i * 10 * 60 * 1000)
            data.value = OxData().setFloat((97 + Random().nextInt(2)).toFloat())
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