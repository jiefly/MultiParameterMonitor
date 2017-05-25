package com.example.jiefly.multiparametermonitor.measuring.data.historydata

import com.example.jiefly.multiparametermonitor.measuring.data.HeartRateData
import java.util.*

/**
 * Created by chgao on 17-5-25.
 */
class HeartRateHistoryData : NormalHistoryData<HeartRateData>() {
    fun setDate(date: Date): HeartRateHistoryData {
        this.date = date
        return this
    }

    fun setValue(value: HeartRateData): HeartRateHistoryData {
        this.value = value
        return this
    }
}