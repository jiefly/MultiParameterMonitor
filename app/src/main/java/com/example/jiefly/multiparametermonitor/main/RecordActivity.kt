package com.example.jiefly.multiparametermonitor.main

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import com.example.jiefly.multiparametermonitor.R

class RecordActivity : AppCompatActivity() {
    var toolbarTitle: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record)
        toolbarTitle = findViewById(R.id.id_tool_bar_title) as TextView?
        toolbarTitle?.text = "历史记录"
        findViewById(R.id.id_tool_bar_left_arrow)?.setOnClickListener { onBackPressed() }
    }
}
