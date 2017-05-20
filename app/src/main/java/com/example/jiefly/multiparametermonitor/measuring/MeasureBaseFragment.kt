package com.example.jiefly.multiparametermonitor.measuring

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import android.widget.Toast
import com.example.jiefly.multiparametermonitor.connection.Connection
import com.example.jiefly.multiparametermonitor.connection.ConnectionActivity
import com.example.jiefly.multiparametermonitor.connection.ConnectionManager
import com.example.jiefly.multiparametermonitor.connection.OnConnectionListener

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

    override fun onDestroy() {
        super.onDestroy()
        if (connection != null) {
            connection!!.releaseCallback(this)
        }
    }

    override fun sendData(s: String?) {}

    override fun sendData(data: CharArray?) {}

    override fun onDataReceived(s: String?) {
        if (s != null) {
            onHandleData(onPerHandleData(s)!!)
        }
    }

    protected abstract fun onHandleData(s: String): Unit

    protected abstract fun onPerHandleData(s: String): String?

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
