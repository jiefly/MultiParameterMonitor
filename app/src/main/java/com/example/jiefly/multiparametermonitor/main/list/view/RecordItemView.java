package com.example.jiefly.multiparametermonitor.main.list.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.jiefly.multiparametermonitor.R;
import com.example.jiefly.multiparametermonitor.main.list.data.NormalItemData;

import static android.widget.RelativeLayout.CENTER_HORIZONTAL;

/**
 * Created by chgao on 17-5-10.
 */

public class RecordItemView extends BaseItemView {
    private FrameLayout mDescriptionFL;
    private TextView mDescriptionOneTv;
    private TextView mDescriptionTwoTv;
    private TextView mLastRecordTimeTv;

    public RecordItemView(Context context) {
        super(context);
    }

    public RecordItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RecordItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.normal_item;
    }

    @Override
    protected void initView() {
        super.initView();
        mDescriptionFL = (FrameLayout) findViewById(R.id.id_item_description);
        mLastRecordTimeTv = (TextView) findViewById(R.id.id_last_record_time);
    }

    @Override
    public void fillData(@NonNull NormalItemData data) {
        super.fillData(data);
        //should show record info
        RelativeLayout.LayoutParams layoutParams;
        if (data.ismRecord()) {
            handleDifferentTypeData(data);
            layoutParams = new RelativeLayout.LayoutParams(getResources().getDimensionPixelSize(R.dimen.small_image_size), getResources().getDimensionPixelSize(R.dimen.small_image_size));
            layoutParams.setMargins(0, getResources().getDimensionPixelSize(R.dimen.small_icon_margin_top), 0, 0);
            layoutParams.addRule(CENTER_HORIZONTAL);
            mDescriptionFL.setVisibility(VISIBLE);
            mLastRecordTimeTv.setVisibility(VISIBLE);
            mDescriptionTwoTv.setText(getResources().getString(data.getmLastRecordUnit()));
            mDescriptionOneTv.setText(data.getmRecordInfo());
            mLastRecordTimeTv.setText(data.getmLastRecordTime());
        } else {
            //hide record info
            layoutParams = new RelativeLayout.LayoutParams(getResources().getDimensionPixelSize(R.dimen.big_image_size), getResources().getDimensionPixelSize(R.dimen.big_image_size));
            layoutParams.setMargins(0, getResources().getDimensionPixelSize(R.dimen.big_icon_margin_top), 0, 0);
            layoutParams.addRule(CENTER_HORIZONTAL);
            mDescriptionFL.setVisibility(GONE);
            mLastRecordTimeTv.setVisibility(GONE);
        }
        mIcon.setLayoutParams(layoutParams);
    }

    private void handleDifferentTypeData(NormalItemData data) {
        mDescriptionFL.removeAllViews();
        if (data.isValueShowHorizontal()) {
            mDescriptionFL.addView(LayoutInflater.from(mContext).inflate(R.layout.main_item_h_record_value_view, mDescriptionFL, false));
        } else {
            mDescriptionFL.addView(LayoutInflater.from(mContext).inflate(R.layout.main_item_vertical_record_value_view, mDescriptionFL, false));
        }
        mDescriptionOneTv = (TextView) findViewById(R.id.id_item_des_1);
        mDescriptionTwoTv = (TextView) findViewById(R.id.id_item_des_2);
    }
}
