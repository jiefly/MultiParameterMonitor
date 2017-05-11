package com.example.jiefly.multiparametermonitor;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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

    private void initView() {
        mConfirmBtn = (Button) findViewById(R.id.id_item_btn);
        mItemNameTv = (TextView) findViewById(R.id.id_item_name_tv);
        mCard = (CardView) findViewById(R.id.id_item_card);
        mIcon = (ImageView) findViewById(R.id.id_item_icon);
    }

    public void setmIconRes(int mIconRes) {
        this.mIconRes = mIconRes;
        mIcon.setImageResource(mIconRes);
    }

    public void setmButtonTextRes(int mButtonTextRes) {
        this.mButtonTextRes = mButtonTextRes;
        mConfirmBtn.setText(getResources().getText(mButtonTextRes));
    }

    public void setmButtonRes(int mButtonRes) {
        this.mButtonRes = mButtonRes;
        mConfirmBtn.setBackgroundResource(mButtonRes);
    }

    public void setmItemNameTextRes(int mItemNameTextRes) {
        this.mItemNameTextRes = mItemNameTextRes;
        mItemNameTv.setText(mItemNameTextRes);
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        mConfirmBtn.setOnClickListener(l);
    }
}
