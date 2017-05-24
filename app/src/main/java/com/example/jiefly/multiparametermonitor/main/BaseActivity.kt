package com.example.jiefly.multiparametermonitor.main

import android.content.DialogInterface
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity

open class BaseActivity : AppCompatActivity() {
    companion object {
        @JvmField
        val DEBUG_MARK = "debug_mark"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    fun showAlert(title: String, message: String, positive: String = "确定", negative: String = "取消", positiveListener: DialogInterface.OnClickListener, negativeListener: DialogInterface.OnClickListener = DialogInterface.OnClickListener { _, _ -> }) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setNegativeButton(positive, positiveListener)
        builder.setPositiveButton(negative, negativeListener)
        builder.show()
    }

    open fun debug(): Boolean {
        return false
    }
}
