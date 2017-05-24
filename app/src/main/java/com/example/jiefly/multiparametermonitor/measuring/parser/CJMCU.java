package com.example.jiefly.multiparametermonitor.measuring.parser;

import android.util.Log;

/**
 * Created by chgao on 17-5-13.
 */

public class CJMCU {
    public static final String TAG = "CJMCU";
    public static final byte DATA_HEAD = 0x66;
    public static final byte OUTPUT_MODE_QUERY = 0x02;
    public static final byte OUTPUT_MODE_CONTINUOUS = 0x01;

    public static String parserData(byte[] raw) {
        if (raw.length < 8) {
            Log.e(TAG, "data is incomplete");
            return "error,data is incomplete";
        }
        int sum = 0;
        if (raw[0] != DATA_HEAD || raw[1] != DATA_HEAD) {
            Log.e(TAG, "data head is wrong,head1:" + raw[0] + "head2:" + raw[1]);
            return "data head is wrong,head1:" + raw[0] + "head2:" + raw[1];
        }
        sum += 2 * DATA_HEAD;
        sum += raw[2];
        sum += raw[3];
        int dataLen = raw[3];
        StringBuilder sb = new StringBuilder();
        for (int i = 4; i < 4 + dataLen; i += 2) {
            sum += raw[i] + raw[i + 1];
            sb.append("temperature：").append((raw[i] * 256 + raw[i + 1]) / 100f).append("℃;");
        }
//        if ((sum&0xff) != raw[3+dataLen+1]){
//            return "data check sum is wrong";
//        }
        return sb.toString();
    }
}
