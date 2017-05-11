package com.example.jiefly.multiparametermonitor.list.data;

import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

import com.example.jiefly.multiparametermonitor.R;

/**
 * Created by chgao on 17-5-10.
 */

public class NormalItemData {
    protected boolean mRecored;
    @LayoutRes
    protected int mIconRes = R.drawable.medical_heart;
    @StringRes
    protected int mButtonTextRes = R.string.test;
    @DrawableRes
    protected int mButtonRes = R.drawable.button_background_selector;
    @StringRes
    protected int mItemNameTextRes = R.string.unknow;
    @LayoutRes
    protected int mLayoutResId = R.layout.normal_item;
    @StringRes
    protected int mLastRecordUnit = R.string.unknow;
    protected String mRecordInfo = "0/0";
    protected String mLastRecordTime = "现在";


    public int getmIconRes() {
        return mIconRes;
    }

    public void setmIconRes(int mIconRes) {
        this.mIconRes = mIconRes;
    }

    public int getmButtonTextRes() {
        return mButtonTextRes;
    }

    public void setmButtonTextRes(int mButtonTextRes) {
        this.mButtonTextRes = mButtonTextRes;
    }

    public int getmButtonRes() {
        return mButtonRes;
    }

    public void setmButtonRes(int mButtonRes) {
        this.mButtonRes = mButtonRes;
    }

    public int getmItemNameTextRes() {
        return mItemNameTextRes;
    }

    public void setmItemNameTextRes( int mItemNameTextRes) {
        this.mItemNameTextRes = mItemNameTextRes;
    }

    public int getmLayoutResId() {
        return mLayoutResId;
    }

    public boolean ismRecored() {
        return mRecored;
    }

    public void setmRecored(boolean mRecored) {
        this.mRecored = mRecored;
    }

    public void setmLayoutResId(int mLayoutResId) {
        this.mLayoutResId = mLayoutResId;
    }

    public int getmLastRecordUnit() {
        return mLastRecordUnit;
    }

    public void setmLastRecordUnit(int mLastRecordUnit) {
        this.mLastRecordUnit = mLastRecordUnit;
    }

    public String getmRecordInfo() {
        return mRecordInfo;
    }

    public void setmRecordInfo(@NonNull String mRecordInfo) {
        this.mRecordInfo = mRecordInfo;
    }

    public String getmLastRecordTime() {
        return mLastRecordTime;
    }

    public void setmLastRecordTime(@NonNull String mLastRecordTime) {
        this.mLastRecordTime = mLastRecordTime;
    }
}
