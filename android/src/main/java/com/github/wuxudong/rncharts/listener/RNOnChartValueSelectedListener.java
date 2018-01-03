package com.github.wuxudong.rncharts.listener;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeArray;
import com.facebook.react.bridge.WritableNativeMap;
import com.facebook.react.uimanager.events.RCTEventEmitter;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.wuxudong.rncharts.utils.EntryToWritableMapUtils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by xudong on 07/03/2017.
 */

public class RNOnChartValueSelectedListener implements OnChartValueSelectedListener {

    private WeakReference<Chart> mWeakChart;
    private Chart chart;

    public RNOnChartValueSelectedListener(Chart chart) {
        mWeakChart = new WeakReference<>(chart);
        this.chart = chart;
    }

    @Override
    public void onValueSelected(Entry entry, Highlight h) {

        if (mWeakChart != null) {
            Chart chart = mWeakChart.get();

            ReactContext reactContext = (ReactContext) chart.getContext();
            reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(
                    chart.getId(),
                    "topSelect",
                    EntryToWritableMapUtils.convertEntryToWritableMap(entry));

            WritableMap map = new WritableNativeMap();
            map.putArray("data", getEntiesForXValue(entry.getX()));
            reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(chart.getId(),"topChange", map);
        }
    }

    @Override
    public void onNothingSelected() {
        if (mWeakChart != null) {
            Chart chart = mWeakChart.get();

            ReactContext reactContext = (ReactContext) chart.getContext();
            reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(
                    chart.getId(),
                    "topSelect",
                    null);
        }

    }


    private WritableArray getEntiesForXValue(float x) {

        WritableArray arr = new WritableNativeArray();

        ArrayList<LineDataSet> dataSets = (ArrayList<LineDataSet>) this.chart.getData().getDataSets();
        for(int i=0; i< dataSets.size(); i++) {

            String label = ((LineDataSet)dataSets.get(i)).getLabel();
            ArrayList<Entry> entries = (ArrayList<Entry>) ((LineDataSet)dataSets.get(i)).getEntriesForXValue(x);

            for(int j=0; j< entries.size(); j++) {

                WritableMap map = new WritableNativeMap();
                map.putString("label", label);
                map.putString("X", entries.get(j).getX() + "");
                map.putString("Y", entries.get(j).getY() + "");
                arr.pushMap(map);
            }
        }
        return  arr;
    }

}
