package com.example.jiefly.multiparametermonitor;

import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.jiefly.multiparametermonitor.list.data.NormalItemData;
import com.example.jiefly.multiparametermonitor.list.view.adapter.MainRecyclerViewAdapter;
import com.qindachang.bluetoothle.BluetoothLe;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private BluetoothLe mBluetoothLe;
    private BluetoothDevice mBluetoothDevice;
    private RecyclerView mRecyclerView;
    private MainRecyclerViewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mRecyclerView = (RecyclerView) findViewById(R.id.id_main_rv);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mAdapter = new MainRecyclerViewAdapter(this);
        mAdapter.addDatas(initData());
        mAdapter.getPositionClicks().observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<NormalItemData>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(NormalItemData data) {
                Log.i(TAG, "item clicked,item name:" + getResources().getString(data.getmItemNameTextRes()));
            }
        });
        mRecyclerView.setAdapter(mAdapter);
        /*mBluetoothLe = BluetoothLe.getDefault();
        if (!mBluetoothLe.isSupportBluetooth()) {
            //设备不支持蓝牙
            Toast.makeText(this,"device not support ble",Toast.LENGTH_SHORT).show();
        } else {
            if (!mBluetoothLe.isBluetoothOpen()) {
                //没有打开蓝牙，请求打开手机蓝牙
                mBluetoothLe.enableBluetooth(this, 666);
            }
        }
        //监听蓝牙回调
        //监听扫描
        mBluetoothLe.setOnScanListener(TAG, new OnLeScanListener() {
            @RequiresApi(api = Build.VERSION_CODES.ECLAIR)
            @Override
            public void onScanResult(BluetoothDevice bluetoothDevice, int rssi, ScanRecord scanRecord) {
                mBluetoothDevice = bluetoothDevice;
                Log.i(TAG, "扫描到设备：" + mBluetoothDevice.getName());
            }

            @Override
            public void onBatchScanResults(List<ScanResult> results) {
                Log.i(TAG, "扫描到设备：" + results.toString());
            }

            @Override
            public void onScanCompleted() {
                Log.i(TAG, "停止扫描");
            }

            @Override
            public void onScanFailed(ScanBleException e) {
                Log.e(TAG, "扫描错误：" + e.toString());
            }
        });
        scan();*/
    }

    @NonNull
    private List<NormalItemData> initData() {
        List<NormalItemData> datas = new ArrayList<>();
        NormalItemData data = new NormalItemData();
        data.setmLayoutResId(R.layout.normal_item);

        data.setmItemNameTextRes(R.string.blood_oxygen);
        data.setmRecored(true);
        data.setmLastRecordUnit(R.string.blood_oxygen_unit);
        data.setmRecordInfo("120/250");
        data.setmLastRecordTime("1小时前");
        data.setmIconRes(R.drawable.heart_with_pulse);
        data.setmButtonTextRes(R.string.record);
        datas.add(data);
        data = new NormalItemData();
        data.setmItemNameTextRes(R.string.blood_pressure);
        datas.add(data);
        data = new NormalItemData();
        data.setmItemNameTextRes(R.string.heart_rate);
        datas.add(data);
        data = new NormalItemData();
        data.setmItemNameTextRes(R.string.temperature);
        datas.add(data);
        data = new NormalItemData();
        data.setmItemNameTextRes(R.string.ecg);
        datas.add(data);
        return datas;
    }

    private void scan() {
        mBluetoothLe.setScanPeriod(20000)
                //   .setScanWithServiceUUID(BluetoothUUID.SERVICE)
                .setReportDelay(0)
                .startScan(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
