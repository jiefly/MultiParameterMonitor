package com.example.jiefly.multiparametermonitor.main.list.data;

import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

import com.example.jiefly.multiparametermonitor.R;

import java.io.Serializable;

/**
 * Created by chgao on 17-5-10.
 */

public class NormalItemData implements Serializable {
    public static String RECORD = "record";
    public static String ICON_RES = "icon_res";
    public static String BUTTON_TEXT_RES = "button_text_res";
    public static String BUTTON_RES = "button_res";
    public static String ITEM_NAME_TEXT_RES = "item_name_text_res";
    public static String LAYOUT_RES = "layout_res";
    public static String RECORD_INFO = "record_info";
    public static String LAST_RECORD_TIME = "last_record_time";

    public enum Type {
        UNKNOW, BLOOD_PRESURE, BLOOD_TEMPUTURE, HEART_REAT, BLOOD_OX, ECG
    }

    public Type mType = Type.UNKNOW;
    protected boolean mRecord;
    @LayoutRes
    protected int mIconRes = R.drawable.ic_heart;
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
    protected String mRecordInfo = "89/123";
    protected String mLastRecordTime = "3 天前";


    public NormalItemData() {
    }


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

    public void setmItemNameTextRes(int mItemNameTextRes) {
        this.mItemNameTextRes = mItemNameTextRes;
    }

    public int getmLayoutResId() {
        return mLayoutResId;
    }

    public void setmLayoutResId(int mLayoutResId) {
        this.mLayoutResId = mLayoutResId;
    }

    public boolean ismRecord() {
        return mRecord;
    }

    public void setmRecord(boolean mRecord) {
        this.mRecord = mRecord;
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

    public Type getmType() {
        return mType;
    }

    public void setmType(Type mType) {
        this.mType = mType;
    }
}
