package com.example.jiefly.multiparametermonitor.measuring.data

/**
 * Created by chgao on 17-5-24.
 */
open class BaseMeasureData {
    open var unit: String = "未知"
    open var value: String = ""
    open fun getShowing(): String {
        return value
    }
}