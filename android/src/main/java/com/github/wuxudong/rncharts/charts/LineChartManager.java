package com.github.wuxudong.rncharts.charts;


import android.util.Log;

import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.wuxudong.rncharts.data.DataExtract;
import com.github.wuxudong.rncharts.data.LineDataExtract;
import com.github.wuxudong.rncharts.listener.RNOnChartValueSelectedListener;
import com.github.wuxudong.rncharts.listener.RNOnChartGestureListener;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class LineChartManager extends BarLineChartBaseManager<LineChart, Entry> {

    @Override
    public String getName() {
        return "RNLineChart";
    }

    @Override
    protected LineChart createViewInstance(ThemedReactContext reactContext) {
        LineChart lineChart =  new LineChart(reactContext);
        lineChart.setOnChartValueSelectedListener(new RNOnChartValueSelectedListener(lineChart));
        lineChart.setOnChartGestureListener(new RNOnChartGestureListener(lineChart));
        return lineChart;
    }

    @ReactProp(name = "xAxis")
    public void setXAxis(LineChart chart, ReadableMap propMap) {
        if (propMap.hasKey("valueFormatter")) {
            ReadableArray xs = propMap.getArray("valueFormatter");
            try {
                Field mLabelCount = AxisBase.class.getDeclaredField("mLabelCount");
                mLabelCount.setAccessible(true);
                mLabelCount.set(chart.getXAxis(), xs.size());

                ArrayList<String> values = new ArrayList<String>();

                for(int i=0; i < xs.size(); i++) {
                    values.add(xs.getString(i));
                }

                MyXFormatter formatter = new MyXFormatter(values.toArray());
                chart.getXAxis().setValueFormatter(formatter);
            } catch (Exception e) {
                Log.e("LineChartManager:", e.getMessage());
            }
        }
    }

    @Override
    DataExtract getDataExtract() {
        return new LineDataExtract();
    }
}

class MyXFormatter implements IAxisValueFormatter {

    private Object[] mValues;

    public MyXFormatter(Object[] values) {
        this.mValues = values;
    }

    private static final String TAG = "MyXFormatter";

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return mValues[(int) value % mValues.length].toString();
    }
}

