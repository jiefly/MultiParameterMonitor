package com.example.jiefly.multiparametermonitor.measuring.data

import java.util.*
import java.util.concurrent.LinkedBlockingQueue

/**
 * Created by chgao on 17-5-22.
 */
open class EcgData : BaseMeasureData() {
    var realValue: Float = 0f
    var realTimeHT: Int = 0
    var averageHT: Int = 0
    var rrInterval: Int = 0
    var rawEcgValues: Queue<Int> = LinkedBlockingQueue()
    var containsHT = false
}