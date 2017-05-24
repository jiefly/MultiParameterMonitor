package com.example.jiefly.multiparametermonitor.measuring

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.jiefly.multiparametermonitor.R
import com.example.jiefly.multiparametermonitor.connection.Connection
import com.example.jiefly.multiparametermonitor.connection.ConnectionActivity
import com.example.jiefly.multiparametermonitor.connection.ConnectionManager
import com.example.jiefly.multiparametermonitor.connection.OnConnectionListener
import com.example.jiefly.multiparametermonitor.measuring.data.EcgData

/**
 * Created by chgao on 17-5-20.
 */
abstract class MeasureBaseFragment : OnConnectionListener<Any>, Fragment() {
    var connection: Connection? = null
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        connection = ConnectionManager.getInstance().connection
        if (connection == null) {
            activity.startActivity(Intent(activity, ConnectionActivity::class.java))
        } else {
            connection!!.registerCallback(this)
        }

    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_measure_incompleted, container, false)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (connection != null) {
            connection!!.releaseCallback(this)
        }
    }

    override fun onDataReceived(s: String?) {
        if (s != null) {
            onHandleData(onPerHandleData(s)!!)
        }
    }

    override fun onDataReceived(data: ByteArray?) {
        data?.let { onHandleData(data) }
    }


    protected abstract fun onHandleData(s: String): Unit

    protected abstract fun onPerHandleData(s: String): String?

    protected open fun onHandleData(data: ByteArray?): Unit {}

    protected open fun onPerHandleData(data: ByteArray): List<EcgData> {
        return ArrayList()
    }

    override fun onDeviceConnecting() {
        toast("设备正在连接")
    }

    override fun onDeviceConnected() {
        toast("设备已连接")
    }

    override fun onDeviceDisconnected() {
        toast("设备已断开连接")
    }

    override fun onDeviceConnectError(message: String?) {
        toast("连接错误:$message")
    }

    fun MeasureBaseFragment.toast(message: CharSequence?, duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(activity, message, duration).show()
    }
}
