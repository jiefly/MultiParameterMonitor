package com.example.jiefly.multiparametermonitor.measuring.data.mock

import com.example.jiefly.multiparametermonitor.R
import com.example.jiefly.multiparametermonitor.application.AppContext
import com.example.jiefly.multiparametermonitor.measuring.data.EcgData

/**
 * Created by chgao on 17-5-24.
 */
class MockEcgData : EcgData(), Mock<MockEcgData> {
    override fun mock(): MockEcgData {
        val stream = AppContext.getResource().openRawResource(R.raw.ecgdata)
        val length = stream.available()
        val buffer = ByteArray(length)
        stream.read(buffer)
        String(buffer).split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray().forEach { rawEcgValues.add(it.toInt()) }
        return this
    }

    init {
        mock()
    }
}