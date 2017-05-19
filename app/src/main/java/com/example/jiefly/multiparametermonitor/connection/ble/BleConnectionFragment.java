package com.example.jiefly.multiparametermonitor.connection.ble;

import android.bluetooth.BluetoothDevice;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.ParcelUuid;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jiefly.multiparametermonitor.R;
import com.example.jiefly.multiparametermonitor.connection.Connection;
import com.example.jiefly.multiparametermonitor.connection.ConnectionActivity;
import com.example.jiefly.multiparametermonitor.connection.OnConnectionListener;
import com.example.jiefly.multiparametermonitor.util.ApiLevelHelper;
import com.example.jiefly.multiparametermonitor.util.LocationUtils;
import com.qindachang.bluetoothle.BluetoothLe;
import com.qindachang.bluetoothle.OnLeScanListener;
import com.qindachang.bluetoothle.exception.ScanBleException;
import com.qindachang.bluetoothle.scanner.ScanRecord;
import com.qindachang.bluetoothle.scanner.ScanResult;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.subjects.PublishSubject;

public class BleConnectionFragment extends Fragment implements View.OnClickListener, OnConnectionListener {
    private static final String TAG = "BleConnectionFragment";
    private List<BluetoothDevice> mScannedDevices = new ArrayList<>();
    private BluetoothLe mBluetoothLe;
    private BluetoothDevice mBluetoothDevice;
    private RecyclerView mRv;
    private MyAdapter mAdapter;
    private PublishSubject<BluetoothDevice> onClickSubject = PublishSubject.create();
    private Connection mConnection;
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mConnection = (Connection) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mConnection.releaseCallback(BleConnectionFragment.this);
        }
    };

    public BleConnectionFragment() {
        onClickSubject.asObservable().observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<BluetoothDevice>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(BluetoothDevice device) {
                mConnection.connectByBle(device, UUID.fromString(UUIDInfo.SERVICE_IO), UUID.fromString(UUIDInfo.CHARACTERISTIC_IO));
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Required empty public constructor
        mBluetoothLe = BluetoothLe.getDefault();
        if (!mBluetoothLe.isSupportBluetooth()) {
            //设备不支持蓝牙
            Toast.makeText(getActivity(), "device not support ble", Toast.LENGTH_SHORT).show();
        } else {
            if (!mBluetoothLe.isBluetoothOpen()) {
                //没有打开蓝牙，请求打开手机蓝牙
                mBluetoothLe.enableBluetooth(getActivity(), 666);
            }
        }
        //监听蓝牙回调
        //监听扫描
        mBluetoothLe.setOnScanListener(TAG, new OnLeScanListener() {
            @RequiresApi(api = Build.VERSION_CODES.ECLAIR)
            @Override
            public void onScanResult(BluetoothDevice bluetoothDevice, int rssi, ScanRecord scanRecord) {
                mBluetoothDevice = bluetoothDevice;
                if (!mScannedDevices.contains(bluetoothDevice)) {
                    mScannedDevices.add(bluetoothDevice);
                    mAdapter.notifyItemInserted(mScannedDevices.size());
                }
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
    }

    private void scan() {
        getActivity().startService(new Intent(getActivity(), BleConnectionService.class));
        getActivity().bindService(new Intent(getActivity(), BleConnectionService.class), mServiceConnection, Context.BIND_AUTO_CREATE);
        //对于Android 6.0以上的版本，申请地理位置动态权限
        if (!((ConnectionActivity) (getActivity())).checkLocationPermission()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("权限需求")
                    .setMessage("Android 6.0 以上的系统版本，扫描蓝牙需要地理位置权限。请允许。")
                    .setNeutralButton("取消", null)
                    .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ((ConnectionActivity) (getActivity())).requestLocationPermission();
                        }
                    })
                    .show();
            return;
        }
        //如果系统版本是7.0以上，则请求打开位置信息
        if (!LocationUtils.isOpenLocService(getActivity()) && ApiLevelHelper.isAtLeast(Build.VERSION_CODES.N)) {
            Toast.makeText(getActivity(), "您的Android版本在7.0以上，扫描需要打开位置信息。", Toast.LENGTH_LONG).show();
            LocationUtils.gotoLocServiceSettings(getActivity());
            return;
        }
        mBluetoothLe.setScanPeriod(20000)
                //   .setScanWithServiceUUID(BluetoothUUID.SERVICE)
                .setReportDelay(0)
                .startScan(getActivity());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ble_connection, container, false);
        mRv = (RecyclerView) view.findViewById(R.id.id_ble_connection_rv);
        initView(view);
        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        getActivity().unbindService(mServiceConnection);
    }

    private void initView(View root) {
        mAdapter = new MyAdapter();
        mRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRv.setAdapter(mAdapter);
        root.findViewById(R.id.id_start_connection).setOnClickListener(this);
        root.findViewById(R.id.id_disconnection).setOnClickListener(this);
        root.findViewById(R.id.id_start_scan).setOnClickListener(this);
        root.findViewById(R.id.id_stop_scan).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_start_scan:
                Log.i(TAG, "开始扫描");
                scan();
                break;
            case R.id.id_stop_scan:
                Log.i(TAG, "停止扫描");
                mBluetoothLe.stopScan();
                break;
            case R.id.id_start_connection:
                break;
            case R.id.id_disconnection:
                Log.i(TAG, "断开连接");
                mConnection.disConnect();
                break;
            default:
        }
    }

    private String getBleDeviceInfo(BluetoothDevice device) {
        StringBuilder sb = new StringBuilder();
        device.fetchUuidsWithSdp();
        sb.append("设备名称：").append(device.getName()).append("\n")
                .append("设备类型：").append(device.getType()).append("\n")
                .append("设备地址：").append(device.getAddress()).append("\n")
                .append("设备带宽：").append(device.getBondState()).append("\n")
                .append("设备UUID信息：").append("\n");
        if (device.getUuids() != null) {
            for (ParcelUuid uuid : device.getUuids()) {
                sb.append("-->").append(uuid.getUuid()).append("\n");
            }
        }
        return sb.toString();
    }

    @Override
    public void sendData(String s) {

    }

    @Override
    public void sendData(char[] data) {

    }

    @Override
    public void onDataReceived(String s) {
        Log.i(TAG, "receive data:" + s);
    }

    @Override
    public void onDeviceConnecting() {
        Log.i(TAG, "ble connecting");
    }

    @Override
    public void onDeviceConnected() {
        Log.i(TAG, "ble connected");

    }

    @Override
    public void onDeviceDisconnected() {
        Log.i(TAG, "ble disconnected");
    }

    @Override
    public void onDeviceConnectError(String message) {
        Log.w(TAG, "ble connect error:" + message);
    }

    private class MyAdapter extends RecyclerView.Adapter<MyVH> {

        @Override
        public MyVH onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MyVH(new TextView(getActivity()));
        }

        @Override
        public void onBindViewHolder(MyVH holder, int position) {
            holder.setText(getBleDeviceInfo(mScannedDevices.get(position)));
            holder.setPosition(position);
        }

        @Override
        public int getItemCount() {
            return mScannedDevices.size();
        }
    }

    private class MyVH extends RecyclerView.ViewHolder {
        private TextView mInfo;
        private int position;

        public MyVH(View itemView) {
            super(itemView);
            mInfo = (TextView) itemView;
            mInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickSubject.onNext(mScannedDevices.get(position));
                }
            });
        }

        public void setPosition(int position) {
            this.position = position;
        }

        public void setText(String s) {
            mInfo.setText(s);
        }
    }

}
