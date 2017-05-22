package com.example.jiefly.multiparametermonitor.measuring.data

/**
 * Created by chgao on 17-5-22.
 */
class EcgData {
    var realValue: Float
    var realTimeHT: Int
    var averageHT: Int
    var rrInterval: Int

    init {
        realTimeHT = 0
        averageHT = 0
        rrInterval = 0
        realValue = 0f
    }

    constructor()
}