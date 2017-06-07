package com.example.jiefly.multiparametermonitor.measuring.data

/**
 * Created by chgao on 17-5-24.
 */
open class OxData : BaseMeasureData() {
    /*氧分压（partial pressure of oxygen,P O2 ）
    为物理溶解于血液的氧所产生的张力。动脉血氧分压（PaO2）约为 13.3kPa(100mmHg）
    静脉血氧分压(PvO2）约为 5.32kPa （ 40mmHg ）， Pa O2 高低主要取决于吸入气体
    的氧分压和外呼吸功能，同时，也是氧向组织弥散的动力因素；而 PvO2 则反映内呼吸功能的状态*/
    var PO2: Float = 0F
    /* 氧容量（oxygen binding capacity ，CO2max ）
    CO2max 指 PaO2 为 19.95kPa （150mmHg) 、PaCO2为 5.32kPa（40mmHg)
    和38℃ 条件下，100ml血液中血红蛋白（Hb）所能结合的最大氧量。CO2max高低取
    决于Hb质和量的影响，反映血液携氧的能力。正常血氧容量约为 8.92mmol/L（20ml%）*/
    var CO2Max: Float = 0F
    /* 氧含量（oxygen content,CO2）
       CO2是指100ml血液的实际带氧量，包括血浆中物理溶解的氧和与 Hb 化学结合的氧。
       当PO2为13.3kPa（100mmHg）时，100ml血浆中呈物理溶解状态的氧约为 0.3ml ，
       化学结合氧约为 19ml。正常动脉血氧含量（CaO2）约为8.47mmol/L（19.3ml/dl) ；
       静脉血氧含量（CvO2）为5.35-6.24mmol/L（12ml%-14ml/dl）。氧含量取决于氧分压
       和Hb的质及量。*/
    var CO2: Float = 0F
    /* 氧饱和度（oxygen saturation ， SO2）
    SO2是指Hb结合氧的百分数。
    SO2 =（氧含量–物理溶解的氧量）/氧容量×100%
    此值主要受PO2的影响，两者之间呈氧合Hb解离曲线的关系。
    正常动脉血氧饱和度为 93%-98% ；静脉血氧饱和度为 70%-75%。*/
    var SO2: Float = 0F

    fun setFloat(value: Float): OxData {
        PO2 = value
        return this
    }

    override var unit: String
        get() = "%"
        set(value) {
            this.unit = value
        }

    override fun getShowing(): String {
        return PO2.toString()
    }
}