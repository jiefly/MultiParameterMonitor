package com.example.jiefly.multiparametermonitor.measuring;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jiefly.multiparametermonitor.R;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A simple {@link Fragment} subclass.
 */
public class MeasureEcg extends Fragment {
    private List<Integer> datas = new ArrayList<Integer>();

    private Queue<Integer> data0Q = new LinkedList<Integer>();
    private Queue<Integer> data1Q = new LinkedList<Integer>();
    public MeasureEcg() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        loadDatas();
        simulator();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_measure_ecg, container, false);
    }

    /**
     * 模拟心电发送，心电数据是一秒500个包，所以
     */
    private void simulator() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if (EcgView.isRunning) {
                    if (data0Q.size() > 0) {
                        EcgView.addEcgData0(data0Q.poll());
                        EcgView.addEcgData1(data1Q.poll());
                    }
                }
            }
        }, 0, 2);
    }

    private void loadDatas() {
        try {
            String data0 = "";
            InputStream in = getResources().openRawResource(R.raw.ecgdata);
            int length = in.available();
            byte[] buffer = new byte[length];
            in.read(buffer);
            data0 = new String(buffer);
            in.close();
            String[] data0s = data0.split(",");
            for (String str : data0s) {
                datas.add(Integer.parseInt(str));
            }

            data0Q.addAll(datas);
            data1Q.addAll(datas);
        } catch (Exception e) {
        }

    }

}
