package com.example.jiefly.multiparametermonitor.measuring.data

/**
 * Created by chgao on 17-5-24.
 */
open class HeartRateData : BaseMeasureData() {
    var heartRate: Int = 0
    fun setRate(value: Int): HeartRateData {
        heartRate = value
        return this
    }

    override var unit: String
        get() = "次/分"
        set(value) {}
}