package com.example.jiefly.multiparametermonitor.measuring.data

/**
 * Created by chgao on 17-5-24.
 */
open class BooldPressureData : BaseMeasureData() {
    /*
    * 根據世界衛生組織於1999年的指引，
    * 120/80以下是理想的收縮壓/舒張壓，
    * 139/89以下是正常血壓，
    * 140/90至160/95是偏高血壓，
    * 161/96以上便屬於高血壓*/
    var low = 80
    var high = 160
}