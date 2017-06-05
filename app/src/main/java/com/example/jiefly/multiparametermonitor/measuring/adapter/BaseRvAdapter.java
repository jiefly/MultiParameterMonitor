package com.example.jiefly.multiparametermonitor.measuring.adapter;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.example.jiefly.multiparametermonitor.R;
import com.example.jiefly.multiparametermonitor.measuring.data.historydata.NormalHistoryData;
import com.example.jiefly.multiparametermonitor.measuring.view.NormalHistoryItemView;

import java.util.ArrayList;
import java.util.List;

public class BaseRvAdapter extends RecyclerView.Adapter<BaseRvAdapter.NormalVH> {
    private Context mContext;
    private List<NormalHistoryData> datas;

    public BaseRvAdapter(Context context) {
        mContext = context;
        datas = new ArrayList<>();
    }

    @Override
    public NormalVH onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NormalVH(new NormalHistoryItemView<NormalHistoryData>(mContext) {
            @Override
            protected int getLayoutRes() {
                return R.layout.heart_rate_history_item;
            }

            @Override
            public void fillData(NormalHistoryData normalHistoryData) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    mTimeTv.setText(normalHistoryData.dateDetail(false, true, true, true, true, false));
                }
                mValueTv.setText(normalHistoryData.getValue().getShowing());
                mValueUnitTv.setText(normalHistoryData.getValue().getUnit());
            }
        });
    }

    @Override
    public void onBindViewHolder(NormalVH holder, int position) {
        holder.getItemView().fillData(datas.get(position));
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public void addData(NormalHistoryData data) {
        if (data != null) {
            datas.add(data);
        }
    }

    public void addDatas(List<NormalHistoryData> datas) {
        if (datas != null) {
            this.datas.addAll(datas);
        }
    }

    class NormalVH extends RecyclerView.ViewHolder {
        NormalHistoryItemView itemView;

        public NormalVH(View itemView) {
            super(itemView);
            this.itemView = (NormalHistoryItemView) itemView;
        }

        public NormalHistoryItemView getItemView() {
            return itemView;
        }
    }
}