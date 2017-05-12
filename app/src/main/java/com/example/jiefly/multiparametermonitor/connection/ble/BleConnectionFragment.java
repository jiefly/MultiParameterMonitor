package com.example.jiefly.multiparametermonitor.connection.ble;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jiefly.multiparametermonitor.R;
import com.qindachang.bluetoothle.BluetoothLe;
import com.qindachang.bluetoothle.OnLeConnectListener;
import com.qindachang.bluetoothle.OnLeNotificationListener;
import com.qindachang.bluetoothle.OnLeReadCharacteristicListener;
import com.qindachang.bluetoothle.OnLeScanListener;
import com.qindachang.bluetoothle.OnLeWriteCharacteristicListener;
import com.qindachang.bluetoothle.exception.BleException;
import com.qindachang.bluetoothle.exception.ConnBleException;
import com.qindachang.bluetoothle.exception.ReadBleException;
import com.qindachang.bluetoothle.exception.ScanBleException;
import com.qindachang.bluetoothle.exception.WriteBleException;
import com.qindachang.bluetoothle.scanner.ScanRecord;
import com.qindachang.bluetoothle.scanner.ScanResult;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.subjects.PublishSubject;

public class BleConnectionFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "BleConnectionFragment";
    private List<BluetoothDevice> mScannedDevices = new ArrayList<>();
    private BluetoothLe mBluetoothLe;
    private BluetoothDevice mBluetoothDevice;
    private RecyclerView mRv;
    private MyAdapter mAdapter;
    private PublishSubject<BluetoothDevice> onClickSubject = PublishSubject.create();

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
                mBluetoothLe.startConnect(device);
            }
        });
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
        mBluetoothLe.setOnConnectListener(new OnLeConnectListener() {
            @Override
            public void onDeviceConnecting() {
                Log.i(TAG, "设备连接中...");
            }

            @Override
            public void onDeviceConnected() {
                Log.i(TAG, "设备连接成功");
            }

            @Override
            public void onDeviceDisconnected() {
                Log.i(TAG, "设备断开连接...");
            }

            @Override
            public void onServicesDiscovered(BluetoothGatt gatt) {
                Log.i(TAG, "发现服务");
                List<BluetoothGattService> services = gatt.getServices();
                for (BluetoothGattService service : services) {
                    Log.i(TAG, "->service uuid:" + service.getUuid());
                    Log.i(TAG, "->service type:" + service.getType());
                    List<BluetoothGattCharacteristic> characteristics = service.getCharacteristics();
                    for (BluetoothGattCharacteristic c : characteristics) {
                        Log.i(TAG, "-->characteristic uuid:" + c.getUuid());
                        if (c.getValue() != null)
                            Log.i(TAG, "-->Characteristic value:" + new String(c.getValue()));
                        List<BluetoothGattDescriptor> descriptors = c.getDescriptors();
                        for (BluetoothGattDescriptor d : descriptors) {
                            Log.i(TAG, "--->Descriptor uuid:" + d.getUuid());
                            if (d.getValue() != null)
                                Log.i(TAG, "--->Descriptor value:" + new String(d.getValue()));
                        }
                    }
                }

                // TODO: 17-5-11 test io
//                BluetoothGattService service = gatt.getService(UUID.fromString(UUIDInfo.SERVICE_IO));
//                BluetoothGattCharacteristic characteristic = service.getCharacteristic(UUID.fromString(UUIDInfo.CHARACTERISTIC_IO));
//                characteristic.setValue("fuck you");
//                gatt.writeCharacteristic(characteristic);
                mBluetoothLe.writeDataToCharacteristic("fuck you!!!".getBytes(), UUIDInfo.SERVICE_IO, UUIDInfo.CHARACTERISTIC_IO);
                mBluetoothLe.readCharacteristic(UUIDInfo.SERVICE_IO, UUIDInfo.CHARACTERISTIC_IO);
//                mBluetoothLe.readCharacteristic("6e400001-b5a3-f393-e0a9-e50e24dcca1e","6e400002-b5a3-f393-e0a9-e50e24dcca1e");

            }

            @Override
            public void onDeviceConnectFail(ConnBleException e) {
                Log.i(TAG, "设备连接失败");
            }
        });
        mBluetoothLe.setOnReadCharacteristicListener(new OnLeReadCharacteristicListener() {
            @Override
            public void onSuccess(BluetoothGattCharacteristic characteristic) {
                Log.i(TAG, "成功读取特征" + characteristic.getValue());
            }

            @Override
            public void onFailure(ReadBleException e) {
                Log.e(TAG, "读取特征失败");
            }
        });
        mBluetoothLe.setOnWriteCharacteristicListener(new OnLeWriteCharacteristicListener() {
            @Override
            public void onSuccess(BluetoothGattCharacteristic characteristic) {
                Log.i(TAG, "成功写入数据" + characteristic.getStringValue(0));
            }

            @Override
            public void onFailed(WriteBleException e) {
                Log.e(TAG, "写入数据失败");
            }
        });
        mBluetoothLe.setOnNotificationListener(new OnLeNotificationListener() {
            @Override
            public void onSuccess(BluetoothGattCharacteristic characteristic) {
                Log.i(TAG, "onNotificationSuccess:" + characteristic.getStringValue(0));
            }

            @Override
            public void onFailed(BleException e) {
                Log.i(TAG, "onNotificationError:" + e.getDetailMessage());
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mBluetoothLe.destroy();
        mBluetoothLe.destroy(TAG);
    }

    private void scan() {
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
                mBluetoothLe.disconnect();
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
