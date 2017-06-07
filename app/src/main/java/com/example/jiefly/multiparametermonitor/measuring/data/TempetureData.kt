package com.example.jiefly.multiparametermonitor.measuring.data

/**
 * Created by chgao on 17-5-24.
 */
open class TempetureData : BaseMeasureData() {
    var temputure = 36.0
    override var unit: String
        get() = "℃"
        set(value) {}

}