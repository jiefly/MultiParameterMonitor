package com.example.jiefly.multiparametermonitor.record.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.jiefly.multiparametermonitor.R

/**
 * Created by chgao on 17-6-3.
 */
open class BaseRecordItemFragment : Fragment() {
    var title: TextView? = null
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_base_record_item, container, false)
        title = view!!.findViewById(R.id.base_fragment_title) as TextView
        return view
    }


    fun setTitle(title: String) {
        this.title!!.text = title
    }
}