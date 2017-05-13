package com.example.jiefly.multiparametermonitor.main.list.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jiefly.multiparametermonitor.main.list.data.NormalItemData;
import com.example.jiefly.multiparametermonitor.main.list.view.BaseItemView;
import com.example.jiefly.multiparametermonitor.main.list.view.RecordItemView;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * Created by chgao on 17-5-10.
 */

public class MainRecyclerViewAdapter extends RecyclerView.Adapter {
    private final Context mContext;
    private final List<NormalItemData> mDatas;
    private final LayoutInflater mLayoutInflater;
    private final PublishSubject<NormalItemData> onClickSubject = PublishSubject.create();
    public MainRecyclerViewAdapter(Context context){
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
        mDatas = new ArrayList<>();
    }

    public List<NormalItemData> addData(@NonNull NormalItemData data){
        mDatas.add(data);
        return mDatas;
    }

    public List<NormalItemData> addDatas(@NonNull List<NormalItemData> datas){
        mDatas.addAll(datas);
        return mDatas;
    }

    public List<NormalItemData> getmDatas(){
        return mDatas;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MainRecyclerItemVH(new RecordItemView(mContext), onClickSubject);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        NormalItemData itemData = mDatas.get(position);
        if (itemData != null) {
            ((MainRecyclerItemVH) holder).resetView(itemData);
        }else {
            throw new NullPointerException("item mData is null!!!");
        }
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public Observable<? extends NormalItemData> getPositionClicks() {
        return onClickSubject.asObservable();
    }

    private class MainRecyclerItemVH extends RecyclerView.ViewHolder{
        BaseItemView baseItemView;
        NormalItemData itemData;
        public MainRecyclerItemVH(View itemView) {
            super(itemView);
            baseItemView = (BaseItemView) itemView;
        }

        public MainRecyclerItemVH(View itemView, PublishSubject<NormalItemData> onClickSubject){
            this(itemView);
            setOnClickListener(onClickSubject);
        }

        protected void setOnClickListener(final PublishSubject<NormalItemData> onClickListener){
            baseItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.onNext(itemData);
                }
            });
        }

        public void resetView(NormalItemData data){
            itemData = data;
            baseItemView.fillData(itemData);
        }
    }
}
