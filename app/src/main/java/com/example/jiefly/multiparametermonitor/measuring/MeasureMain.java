package com.example.jiefly.multiparametermonitor.measuring;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.jiefly.multiparametermonitor.R;
import com.example.jiefly.multiparametermonitor.main.BaseActivity;
import com.example.jiefly.multiparametermonitor.main.list.data.NormalItemData;

public class MeasureMain extends BaseActivity {
    public final static String MEASURE_TYPE = "measure_type";
    private static final String TAG = MeasureMain.class.getSimpleName();
    private TextView mTitle;
    private boolean DEBUG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measure_main);
        mTitle = (TextView) findViewById(R.id.id_normal_tool_bar).findViewById(R.id.id_tool_bar_title);
        findViewById(R.id.id_normal_tool_bar).findViewById(R.id.id_tool_bar_left_arrow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        DEBUG = getIntent().getBooleanExtra(DEBUG_MARK, false);
        startMeasureFragment(NormalItemData.Type.getTypeFromValue(getIntent().getIntExtra(MEASURE_TYPE, NormalItemData.Type.UNKNOW.getValue())));
    }

    private void startMeasureFragment(NormalItemData.Type type) {
        mTitle.setText(NormalItemData.Type.getShowStringByType(type));
        FragmentManager manager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = manager.beginTransaction();
        switch (type) {
            case BLOOD_OX:
                transaction.replace(R.id.id_container, new MeasureOx(), "MeasureOx");
                break;
            case BLOOD_TEMPUTURE:
                transaction.replace(R.id.id_container, new MeasureTemperature(), "MeasureTemperature");
                break;
            case HEART_RATE:
                transaction.replace(R.id.id_container, new MeasureHeartRate(), "MeasureHeartRate");

                break;
            case BLOOD_PRESSURE:
                transaction.replace(R.id.id_container, new MeasureBloodPressure(), "MeasureBloodPressure");
                break;
            case ECG:
                transaction.replace(R.id.id_container, new MeasureEcg(), "MeasureEcg");
                break;
            default:
                Log.w(TAG, "unKnow measure type");
                return;
        }
        transaction.commit();
    }

    @Override
    public boolean debug() {
        return DEBUG;
    }
}
