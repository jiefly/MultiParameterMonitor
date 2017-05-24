package com.example.jiefly.multiparametermonitor.measuring.parser

import com.example.jiefly.multiparametermonitor.measuring.data.*
import com.example.jiefly.multiparametermonitor.measuring.data.mock.MockEcgData

/**
 * Created by chgao on 17-5-24.
 */
class ParserHelper {
    fun parserBooldPressure(data: ByteArray, mock: Boolean = false): BooldPressureData {
        return BooldPressureData()
    }

    fun parserEcg(data: ByteArray, mock: Boolean = false): EcgData {
        if (mock) {
            return MockEcgData()
        }
        return EcgData()
    }

    fun parserTemperature(data: ByteArray, mock: Boolean = false): TempetureData {
        return TempetureData()
    }

    fun parserHeartRate(data: ByteArray, mock: Boolean = false): HeartRateData {
        return HeartRateData()
    }

    fun parserOX(data: ByteArray, mock: Boolean = false): OxData {
        return OxData()
    }
}
