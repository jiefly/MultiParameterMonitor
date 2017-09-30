package com.example.jiefly.multiparametermonitor.test;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.jiefly.multiparametermonitor.R;
import com.example.jiefly.multiparametermonitor.connection.Connection;
import com.example.jiefly.multiparametermonitor.connection.OnConnectionListener;
import com.example.jiefly.multiparametermonitor.measuring.data.EcgData;
import com.example.jiefly.multiparametermonitor.measuring.parser.BMD101;
import com.example.jiefly.multiparametermonitor.measuring.parser.ParserHelper;
import com.example.jiefly.multiparametermonitor.measuring.view.EcgView;
import com.example.jiefly.multiparametermonitor.util.jwave.Transform;
import com.example.jiefly.multiparametermonitor.util.jwave.TransformBuilder;

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

    private Queue<Float> data0Q = new LinkedList<Float>();
    private Queue<Float> data1Q = new LinkedList<Float>();
    private Lock lock;
    private Condition write;
    private Condition read;
    private Connection mConnection;
    private int flag = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lock = new ReentrantLock();
        write = lock.newCondition();
        read = lock.newCondition();
        setContentView(R.layout.activity_ecgtest);
        try {
            loadDataFromBin();
        } catch (IOException e) {
            e.printStackTrace();
        }
        simulator();
//        mConnection= ConnectionManager.getInstance().getConnectionListener();
//        if (mConnection != null){
//            mConnection.registerCallback(this);
//        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mConnection != null) {
            mConnection.registerCallback(this);
        }
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
                    if (!data0Q.isEmpty() && !data1Q.isEmpty()) {
                        EcgView.addEcgData0(data0Q.poll());
                        EcgView.addEcgData1(data1Q.poll());
                    }
                    lock.unlock();
                }
            }
        }, 0, 2);
    }


    private void loadDataFromBin() throws IOException {
        InputStream in = getResources().openRawResource(R.raw.ecg_data_hz);
        List<Integer> datas = new ArrayList<>();
        for (int i = 0, len = in.available(); i < len; i++) {
            datas.add(in.read());
        }
        List<EcgData> data = new BMD101().parserEcgData(datas);
        /*List<EcgData> data = new ArrayList<>();
        Queue<Integer> q = new ParserHelper().parserEcg(true).getRawEcgValues();
        while (!q.isEmpty()) {
            EcgData data1 = new EcgData();
            data1.setRealValue(q.poll());
            data.add(data1);
        }*/
        for (EcgData ecgData : data) {
            ecgData.setRealValue(ecgData.getRealValue());
            data1Q.add(ecgData.getRealValue());
        }
        double[] filtered = filterData(data);
        for (double d : filtered) {
            data0Q.add((float) d);
        }
    }

    private double[] filterData(List<EcgData> data) {
        String waveletIdentifier;
        String transformIdentifier; // raw n dirty but working

        transformIdentifier = "Fast Wavelet Transform";
        waveletIdentifier = "Daubechies 5";
        Transform transform =
                TransformBuilder.create(transformIdentifier, waveletIdentifier);
        int x = 0;
        while (Math.pow(2, ++x) < data.size()) ;
        double[] arrTime = new double[(int) Math.pow(2, x - 1)];
        for (int i = 0; i < arrTime.length; i++) {
            arrTime[i] = data.get(i).getRealValue();
        }
        double[] arrFreqOrHilb = transform.forward(arrTime);
        double[] filtered = transform.reverse(arrFreqOrHilb);
        return filtered;

    }

    /*private void loadDatas() {
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

    }*/

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
        /*for (int i = from; i < datas.length; i++) {
            if (datas[i] > '9' || datas[i] < '0' && i > from) {
                data0Q.add(Integer.parseInt(new String(datas, from, i - from)));
                from = i + 1;
            }
        }
        if (from < datas.length) {
            data0Q.add(Integer.parseInt(new String(datas, from, datas.length - from)));
        }*/
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
