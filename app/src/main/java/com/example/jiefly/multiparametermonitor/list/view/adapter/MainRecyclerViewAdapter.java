package com.example.jiefly.multiparametermonitor.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jiefly.multiparametermonitor.BaseItemView;
import com.example.jiefly.multiparametermonitor.R;

/**
 * Created by chgao on 17-5-10.
 */

public class MainRecyclerViewAdapter extends RecyclerView.Adapter {
    private final Context mContext;
    private final LayoutInflater mLayoutInflater;
    public MainRecyclerViewAdapter(Context context){
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MainRecyclerItemVH(mLayoutInflater.inflate(R.layout.normal_item,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class MainRecyclerItemVH extends RecyclerView.ViewHolder{
        BaseItemView baseItemView;
        public MainRecyclerItemVH(View itemView) {
            super(itemView);
            baseItemView = (BaseItemView) itemView;
        }
    }
}
