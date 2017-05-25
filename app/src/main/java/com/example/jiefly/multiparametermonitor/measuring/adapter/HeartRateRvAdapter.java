package com.example.jiefly.multiparametermonitor.measuring.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.example.jiefly.multiparametermonitor.measuring.data.historydata.HeartRateHistoryData;
import com.example.jiefly.multiparametermonitor.measuring.view.HeartRateHistoryItemView;

import java.util.ArrayList;
import java.util.List;

public class HeartRateRvAdapter extends RecyclerView.Adapter<HeartRateRvAdapter.HeartRateVH> {
    private Context mContext;
    private List<HeartRateHistoryData> datas;

    public HeartRateRvAdapter(Context context) {
        mContext = context;
        datas = new ArrayList<>();
    }

    @Override
    public HeartRateVH onCreateViewHolder(ViewGroup parent, int viewType) {
        return new HeartRateVH(new HeartRateHistoryItemView(mContext));
    }

    @Override
    public void onBindViewHolder(HeartRateVH holder, int position) {
        holder.getItemView().fillData(datas.get(position));
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public void addData(HeartRateHistoryData data) {
        if (data != null) {
            datas.add(data);
        }
    }

    public void addDatas(List<HeartRateHistoryData> datas) {
        if (datas != null) {
            this.datas.addAll(datas);
        }
    }

    class HeartRateVH extends RecyclerView.ViewHolder {
        HeartRateHistoryItemView itemView;

        public HeartRateVH(View itemView) {
            super(itemView);
            this.itemView = (HeartRateHistoryItemView) itemView;
        }

        public HeartRateHistoryItemView getItemView() {
            return itemView;
        }
    }
}