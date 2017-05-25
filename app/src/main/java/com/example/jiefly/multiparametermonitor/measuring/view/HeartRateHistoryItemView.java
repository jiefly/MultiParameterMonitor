package com.example.jiefly.multiparametermonitor.measuring.view;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;

import com.example.jiefly.multiparametermonitor.R;
import com.example.jiefly.multiparametermonitor.measuring.data.historydata.HeartRateHistoryData;

/**
 * Created by chgao on 17-5-25.
 */

public class HeartRateHistoryItemView extends NormalHistoryItemView<HeartRateHistoryData> {
    public HeartRateHistoryItemView(Context context) {
        super(context);
    }

    public HeartRateHistoryItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HeartRateHistoryItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.heart_rate_history_item;
    }

    @Override
    public void fillData(HeartRateHistoryData heartRateHistoryData) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mTimeTv.setText(heartRateHistoryData.dateDetail(false, true, true, true, true, false));
        }
        mValueTv.setText(heartRateHistoryData.getValue().getHeartRate() + "");
        mValueUnitTv.setText(heartRateHistoryData.getValue().getUnit());
    }


}
