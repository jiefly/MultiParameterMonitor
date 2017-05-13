package com.example.jiefly.multiparametermonitor.main.list.view;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.jiefly.multiparametermonitor.R;
import com.example.jiefly.multiparametermonitor.main.list.data.NormalItemData;

/**
 * Created by chgao on 17-5-9.
 */

public abstract class BaseItemView extends LinearLayout{
    public static int INVAILD_ID = -1;
    @LayoutRes
    protected int mIconRes;
    @StringRes
    protected int mButtonTextRes;
    @DrawableRes
    protected int mButtonRes;
    @StringRes
    protected int mItemNameTextRes;
    @LayoutRes
    protected int mLayoutResId = INVAILD_ID;

    protected Button mConfirmBtn;
    protected TextView mItemNameTv;
    protected CardView mCard;
    protected ImageView mIcon;
    protected NormalItemData mData;

    public BaseItemView(Context context) {
        super(context);
        init(context);
    }

    public BaseItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BaseItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    protected abstract int getLayoutRes();

    private void init(Context context){
        mLayoutResId = getLayoutRes();
        if (mLayoutResId == INVAILD_ID){
            mLayoutResId = R.layout.normal_item;
        }
        inflate(context,mLayoutResId,this);

        initView();
    }

    protected void initView() {
        mConfirmBtn = (Button) findViewById(R.id.id_item_btn);
        mItemNameTv = (TextView) findViewById(R.id.id_item_name_tv);
        mCard = (CardView) findViewById(R.id.id_item_card);
        mIcon = (ImageView) findViewById(R.id.id_item_icon);
    }
    public void fillData(@NonNull NormalItemData data){
        mData = data;
        mIcon.setImageResource(data.getmIconRes());
        mConfirmBtn.setText(getResources().getText(data.getmButtonTextRes()));
        mConfirmBtn.setBackgroundResource(data.getmButtonRes());
        mItemNameTv.setText(getResources().getText(data.getmItemNameTextRes()));
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        mConfirmBtn.setOnClickListener(l);
    }
}
