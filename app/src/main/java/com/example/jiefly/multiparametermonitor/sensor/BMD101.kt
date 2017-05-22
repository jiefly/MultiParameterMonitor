package com.example.jiefly.multiparametermonitor.sensor

import android.util.Log.e
import com.example.jiefly.multiparametermonitor.measuring.data.EcgData

/**
 * Created by chgao on 17-5-22.
 */
class BMD101 {
    val TAG = "BMD101"
    val MAX_UNIT_LEN = 173
    val MIN_UNIT_LEN = 4
    val SYNC = 0xaa
    val EXCODE = 0x55
    //no byte
    val POOR_SIGNAL = 0x02
    //no　byte
    val CURRENT_HEART_RATE = 0x03
    //no byte
    val CONFIG_BYTE = 0x08
    //2 byte
    val ECG_RAW = 0x80
    // 5 byte
    val MUL_HEART_RATE = 0x84
    //3 byte
    val MUL_CONFIG_BYTE = 0x85

    fun parserEcgData(srcData: Array<Int>): List<EcgData> {
        var result = ArrayList<EcgData>()
        var units: List<Array<Int>> = splitUnit(srcData)
        var ecg: EcgData
        var unitLen = 0
        for (unit in units) {
            if (unit.size < MIN_UNIT_LEN || unit.size > MAX_UNIT_LEN) {
                e(TAG, "unit len is wrong,size:$unitLen did't range in [ $MIN_UNIT_LEN,$MAX_UNIT_LEN ]")
                continue
            }
            unitLen = unit[2] + 4
            if (unit.size != unitLen) {
                e(TAG, "unit real length did't match length in data,real length is ${unit.size},want length is $unitLen")
                continue
            }
            ecg = EcgData()
            //remove check sum
            var i = 3
            while (i < unit.size - 2) {
                when (unit[i]) {
                //单变量字节
                    POOR_SIGNAL -> {
                        i += 2
                    }
                //单变量字节
                    CURRENT_HEART_RATE -> {
                        ecg.realTimeHT = unit[i + 1]
                        i += 2
                    }
                //单变量字节
                    CONFIG_BYTE -> {
                        i += 2
                    }
                    MUL_HEART_RATE -> {
                        //平均心率数据长度为５
                        if (unit[i + 1] == 0x05) {
                            var sum = 0
                            for (j in i + 2..i + 6) {
                                sum += unit[i]
                            }
                            ecg.averageHT = sum / 5
                        }
                        i += 2 + unit[i + 1]
                    }
                    ECG_RAW -> {
                        //心电信号数据长度为２
                        if (unit[i + 1] == 0x02) {
                            ecg.realValue = (unit[i + 2] * 256 + unit[i + 3]).toFloat()
                        }
                        i += 2 + unit[i + 1]
                    }

                    MUL_CONFIG_BYTE -> {
                        i += 2 + unit[i + 1]
                    }
                    EXCODE -> {
                        i++
                    }
                    else -> i++
                }
            }
            result.add(ecg)
        }
        return result
    }

    fun splitUnit(srcData: Array<Int>): List<Array<Int>> {
        val result = ArrayList<Array<Int>>()

        //find start
        //srcData[i] == srcData[i+1] == SYNC
        var from = findHead(srcData)
        var i = from + 1
        while (i < srcData.size) {
            i = findHead(srcData, from = i) + 2
            if (i - 3 > from) {
                result.add(srcData.copyOfRange(from, i - 3))
                from = i - 2
            } else {
                //can't find next head
                result.add(srcData.copyOfRange(from, srcData.size - 1))
                return result
            }
        }
        return result
    }

    fun findHead(srcData: Array<Int>, from: Int = 0, to: Int = srcData.size - 1): Int {
        return (from..to).firstOrNull { srcData[it] == SYNC && it < to && srcData[it + 1] == SYNC } ?: 0
    }

}

fun main(args: Array<String>) {
    val test = "aa aa 04 80 02 07 87 ef aa aa 04 80 02 05 28 50 aa aa 04 80 02 04 ea 8f aa aa 04 80 02 06 c3 b4 aa aa 04 80 02 07 5d 19"
    val input: Array<Int> = Array(100, { i -> if (i == 1 || i == 0) 0xaa else 0xff })
    val mockData = ArrayList<Int>()
    test.split(" ").forEach {
        var value: Int = -1
        for (c in it.toCharArray()) {
            when (c) {
                in 'a'..'f' -> {
                    if (value != -1) {
                        value += (c - 'a' + 10) * 16 + 1
                    } else {
                        value += (c - 'a' + 10)
                    }
                }
                in '0'..'9' -> {
                    if (value != -1) {
                        value += (c - '0') * 16 + 1
                    } else {
                        value += (c - '0')
                    }
                }
            }
        }
        mockData.add(value)
    }
    BMD101().parserEcgData(Array(mockData.size, { i -> mockData[i] }))
}