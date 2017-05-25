package com.example.jiefly.multiparametermonitor.measuring.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.jiefly.multiparametermonitor.R;

/**
 * Created by chgao on 17-5-25.
 */

public abstract class NormalHistoryItemView<D> extends LinearLayout {
    private static final int INVALID = -1;
    protected Context mContext;
    protected TextView mTimeTv;
    protected TextView mValueTv;
    protected TextView mValueUnitTv;
    protected ImageView mLeftArrowIv;
    private int mLayoutResId;

    public NormalHistoryItemView(Context context) {
        super(context);
        init(context);
    }

    public NormalHistoryItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public NormalHistoryItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        if (getLayoutRes() == INVALID) {
            mLayoutResId = R.layout.heart_rate_history_item;
        } else {
            mLayoutResId = getLayoutRes();
        }
        inflate(context, mLayoutResId, this);
        initView();
    }

    private void initView() {
        mTimeTv = (TextView) findViewById(R.id.id_record_date);
        mValueTv = (TextView) findViewById(R.id.id_heart_rate_value);
        mValueUnitTv = (TextView) findViewById(R.id.id_heart_rate_unit);
        mLeftArrowIv = (ImageView) findViewById(R.id.id_heart_rate_detail);
    }

    abstract protected int getLayoutRes();

    abstract public void fillData(D d);
}
