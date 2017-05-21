package com.example.jiefly.multiparametermonitor.main;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.example.jiefly.multiparametermonitor.R;
import com.example.jiefly.multiparametermonitor.connection.ConnectionActivity;
import com.example.jiefly.multiparametermonitor.connection.ConnectionManager;
import com.example.jiefly.multiparametermonitor.main.list.data.NormalItemData;
import com.example.jiefly.multiparametermonitor.main.list.view.adapter.MainRecyclerViewAdapter;
import com.example.jiefly.multiparametermonitor.measuring.MeasureMain;
import com.example.jiefly.multiparametermonitor.util.ObjectHelper;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

public class MainActivity extends AppCompatActivity {
    public static final int CONNECTION = 0x1001;
    public static final int CONNECTION_AND_TO_MEASURE = 0x1010;
    private RecyclerView mRecyclerView;
    private MainRecyclerViewAdapter mAdapter;
    private Toolbar mToolbar;
    private TextView mToolbarTitle;
    private FloatingActionsMenu mMenu;
    private NormalItemData.Type mWillMeasureType;
    private View mCover;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        initToolBar();
        initMenu();
        mCover = findViewById(R.id.id_main_cover);
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
                if (ConnectionManager.getInstance().isConnected()) {
                    jumpToMeasure(data.getmType());
                } else {
                    showConnectDeviceDialog(data);
                }
            }
        });
        mRecyclerView.setAdapter(mAdapter);
    }

    private void jumpToMeasure(NormalItemData.Type type) {
        Intent intent = new Intent(this, MeasureMain.class);
        intent.putExtra(MeasureMain.MEASURE_TYPE, type.getValue());
        startActivity(intent);
    }

    private void showConnectDeviceDialog(NormalItemData data) {
        mWillMeasureType = data.getmType();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("当前设备未连接");
        builder.setMessage("在测量" + getResources().getString(data.getmItemNameTextRes()) + "前请先连接上设备");
        builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivityForResult(new Intent(MainActivity.this, ConnectionActivity.class), CONNECTION_AND_TO_MEASURE);
            }
        });
        builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CONNECTION:
                    break;
                case CONNECTION_AND_TO_MEASURE:
                    startActivity(new Intent(MainActivity.this, MeasureMain.class).putExtra(MeasureMain.MEASURE_TYPE, mWillMeasureType.getValue()));
                    break;
                default:
            }
        }
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        ConnectionManager.getInstance().registerListener(new OnConnectedListener() {
            @Override
            public void onConnected() {
                setConnectionStatus();
            }

            @Override
            public void onDisconnected() {
                setConnectionStatus();
            }
        });
        return super.onCreateView(name, context, attrs);
    }

    private void toggleCover(boolean show) {
        if (mCover == null) {
            mCover = findViewById(R.id.id_main_cover);
        }
        if (show) {
            mCover.setVisibility(View.VISIBLE);
            mCover.setAlpha(0.9f);
        } else {
            mCover.setVisibility(View.GONE);
        }
    }
    private void initMenu() {
        mMenu = (FloatingActionsMenu) findViewById(R.id.id_main_menu);
        mMenu.setOnFloatingActionsMenuUpdateListener(new FloatingActionsMenu.OnFloatingActionsMenuUpdateListener() {
            @Override
            public void onMenuExpanded() {
                toggleCover(true);
            }

            @Override
            public void onMenuCollapsed() {
                toggleCover(false);
            }
        });
        mMenu.findViewById(R.id.id_main_menu_item_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMenu.collapse();
                ConnectionManager.getInstance().setupConnection(MainActivity.this);
            }
        });
        FloatingActionButton actionButton = (FloatingActionButton) mMenu.findViewById(R.id.id_main_menu_item_2);
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMenu.collapse();
                startActivity(new Intent(MainActivity.this, RecordActivity.class));
            }
        });
        mMenu.findViewById(R.id.id_main_menu_item_3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMenu.collapse();
                Snackbar.make(mMenu, "menu 3 clicked", Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private void initToolBar() {
        mToolbar = (Toolbar) findViewById(R.id.id_tool_bar);
        mToolbarTitle = (TextView) mToolbar.findViewById(R.id.id_tool_bar_title);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");
        mToolbarTitle.setText(ConnectionManager.getInstance().isConnected() ? "设备已连接" : "设备未连接");
    }

    @NonNull
    private List<NormalItemData> initData() {
        List<NormalItemData> datas = new ArrayList<>();
        //sharedPreferences init in application
        SharedPreferences sharedPreferences = getApplication().getSharedPreferences("", 0);
        for (NormalItemData.Type t : NormalItemData.Type.values()) {
            NormalItemData data = (NormalItemData) ObjectHelper.readObject(sharedPreferences, t.toString());
            if (data != null) {
                datas.add(data);
            } else {
                data = new NormalItemData();
                data.setmType(t);
                switch (t) {
                    case UNKNOW:
                        continue;
                    case BLOOD_OX:
                        data.setmItemNameTextRes(R.string.blood_oxygen);
                        data.setmRecord(false);
                        data.setmLastRecordUnit(R.string.blood_oxygen_unit);
                        data.setmIconRes(R.drawable.ic_oxygen_symbol);
                        break;
                    case BLOOD_PRESSURE:
                        data.setmItemNameTextRes(R.string.blood_pressure);
                        data.setmIconRes(R.drawable.ic_sphygmomanometer);
                        break;
                    case BLOOD_TEMPUTURE:
                        data.setmItemNameTextRes(R.string.temperature);
                        data.setmRecord(false);
                        data.setmIconRes(R.drawable.ic_thermometer_1);
                        data.setmLastRecordUnit(R.string.temperature_unit);
                        data.setmLastRecordTime("4 天前");
                        data.setmRecordInfo("35.7");
                        break;
                    case ECG:
                        data.setmItemNameTextRes(R.string.ecg);
                        data.setmIconRes(R.drawable.ic_ecg);
                        break;
                    case HEART_RATE:
                        data.setmItemNameTextRes(R.string.heart_rate);
                        data.setmIconRes(R.drawable.ic_cardiogram);
                        data.setmRecord(false);
                        break;
                }
                datas.add(data);
            }
        }
//        NormalItemData data = new NormalItemData();
//        data.setmLayoutResId(R.layout.normal_item);
//        data.setmItemNameTextRes(R.string.blood_oxygen);
//        data.setmRecord(true);
//        data.setmLastRecordUnit(R.string.blood_oxygen_unit);
//        data.setmRecordInfo("120/250");
//        data.setmLastRecordTime("1小时前");
//        data.setmIconRes(R.drawable.heart_with_pulse);
//        data.setmButtonTextRes(R.string.record);
//        datas.add(data);
//        data = new NormalItemData();
//        data.setmItemNameTextRes(R.string.blood_pressure);
//        datas.add(data);
//        data = new NormalItemData();
//        data.setmItemNameTextRes(R.string.heart_rate);
//        datas.add(data);
//        data = new NormalItemData();
//        data.setmItemNameTextRes(R.string.temperature);
//        datas.add(data);
//        data = new NormalItemData();
//        data.setmItemNameTextRes(R.string.ecg);
//        datas.add(data);
        return datas;
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    public void setConnectionStatus() {
        Observable.just("").observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                mToolbarTitle.setText(ConnectionManager.getInstance().isConnected() ? "设备已连接" : "设备未连接");
            }
        });
    }

    public interface OnConnectedListener {
        void onConnected();

        void onDisconnected();
    }
}
