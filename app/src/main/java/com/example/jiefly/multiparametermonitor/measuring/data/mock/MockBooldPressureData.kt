package com.example.jiefly.multiparametermonitor.measuring.data.mock

import com.example.jiefly.multiparametermonitor.measuring.data.BooldPressureData
import java.util.*

/**
 * Created by chgao on 17-5-24.
 */
class MockBooldPressureData : BooldPressureData(), Mock<BooldPressureData> {
    override fun mock(): BooldPressureData {
        var data = BooldPressureData()
        data.high = Random().nextInt(30) + 145
        data.low = Random().nextInt(30) + 65
        return data
    }
}