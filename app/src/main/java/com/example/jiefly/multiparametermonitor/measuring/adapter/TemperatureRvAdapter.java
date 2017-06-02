package com.example.jiefly.multiparametermonitor.measuring.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.example.jiefly.multiparametermonitor.measuring.data.historydata.TemperatureHistoryData;
import com.example.jiefly.multiparametermonitor.measuring.view.TemperatureHistoryItemView;

import java.util.ArrayList;
import java.util.List;

public class TemperatureRvAdapter extends RecyclerView.Adapter<TemperatureRvAdapter.TemperatureVH> {
    private Context mContext;
    private List<TemperatureHistoryData> datas;

    public TemperatureRvAdapter(Context context) {
        mContext = context;
        datas = new ArrayList<>();
    }

    @Override
    public TemperatureVH onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TemperatureVH(new TemperatureHistoryItemView(mContext));
    }

    @Override
    public void onBindViewHolder(TemperatureVH holder, int position) {
        holder.getItemView().fillData(datas.get(position));
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public void addData(TemperatureHistoryData data) {
        if (data != null) {
            datas.add(data);
        }
    }

    public void addDatas(List<TemperatureHistoryData> datas) {
        if (datas != null) {
            this.datas.addAll(datas);
        }
    }

    class TemperatureVH extends RecyclerView.ViewHolder {
        TemperatureHistoryItemView itemView;

        public TemperatureVH(View itemView) {
            super(itemView);
            this.itemView = (TemperatureHistoryItemView) itemView;
        }

        public TemperatureHistoryItemView getItemView() {
            return itemView;
        }
    }
}