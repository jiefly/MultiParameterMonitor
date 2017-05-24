package com.example.jiefly.multiparametermonitor;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.jiefly.multiparametermonitor.connection.Connection;
import com.example.jiefly.multiparametermonitor.connection.OnConnectionListener;
import com.example.jiefly.multiparametermonitor.measuring.EcgView;
import com.example.jiefly.multiparametermonitor.measuring.data.EcgData;
import com.example.jiefly.multiparametermonitor.sensor.BMD101;
import com.jonas.jgraph.graph.JcoolGraph;
import com.jonas.jgraph.models.Jchart;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ECGTestActivity extends AppCompatActivity implements OnConnectionListener {
    private List<Integer> datas = new ArrayList<Integer>();
    private List<Integer> data1Datas = new ArrayList<Integer>();

    private Queue<Integer> data0Q = new LinkedList<Integer>();
    private Queue<Integer> data1Q = new LinkedList<Integer>();
    private Lock lock;
    private Condition write;
    private Condition read;
    private Connection mConnection;
    private int flag = 0;

    private JcoolGraph jcoolGraph;
    private List<Jchart> jcharts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lock = new ReentrantLock();
        write = lock.newCondition();
        read = lock.newCondition();
        setContentView(R.layout.activity_ecgtest);
        loadDatafromBin();
        simulator();
//        mConnection= ConnectionManager.getInstance().getConnectionListener();
//        if (mConnection != null){
//            mConnection.registerCallback(this);
//        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        if (mConnection!=null) {
//            mConnection.registerCallback(this);
//        }
    }

    /**
     * 模拟心电发送，心电数据是一秒500个包，所以
     */
    private void simulator() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if (EcgView.isRunning) {
//                    Log.i("ECG_TEST_ACTIVITY","data0Q size:"+data0Q.size());
                    lock.lock();
                    if (!data0Q.isEmpty()) {
                        EcgView.addEcgData0(data0Q.poll());
                        EcgView.addEcgData1(data1Q.poll());
                    }
                    lock.unlock();
                }
            }
        }, 0, 2);
    }

    private void loadDatafromBin() {
        try {
            InputStream in = getResources().openRawResource(R.raw.ecg_bin);
            int len = in.available();
            List<Integer> datas = new ArrayList<>();
            for (int i = 0; i < len; i++) {
                datas.add(in.read());
            }
            List<EcgData> data = new BMD101().parserEcgData(datas);
            ArrayList<Double> filters = new ArrayList<>();
            for (EcgData ecgData : data) {
                filters.add((double) ecgData.getRealValue() + 10000);
            }
            filters = new KalmanFilter(filters).calc();
            for (EcgData ecgData : data) {
                data1Q.add((int) ecgData.getRealValue() + 10000);
            }
            for (double x : filters) {
                data0Q.add((int) x);
                Log.i("parsered", "实时数据" + x);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadDatas() {
        try {
            String data0 = "";
            InputStream in = getResources().openRawResource(R.raw.ok_ecg_01);
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

    @Override
    public void onDataReceived(String s) {
        if (s == null || s.length() == 0) {
            return;
        }
        Log.i("data received in ECG", s);
        char[] datas = s.toCharArray();
        int from = 0;
        for (int i = 0; i < datas.length; i++) {
            if (datas[i] <= '9' && datas[i] >= '0') {
                from = i;
                break;
            }
        }
        lock.lock();
        for (int i = from; i < datas.length; i++) {
            if (datas[i] > '9' || datas[i] < '0' && i > from) {
                data0Q.add(Integer.parseInt(new String(datas, from, i - from)));
                from = i + 1;
            }
        }
        if (from < datas.length) {
            data0Q.add(Integer.parseInt(new String(datas, from, datas.length - from)));
        }
        lock.unlock();
    }

    @Override
    public void onDataReceived(byte[] data) {

    }

    @Override
    public void onDeviceConnecting() {

    }

    @Override
    public void onDeviceConnected() {

    }

    @Override
    public void onDeviceDisconnected() {

    }

    @Override
    public void onDeviceConnectError(String message) {

    }
}
