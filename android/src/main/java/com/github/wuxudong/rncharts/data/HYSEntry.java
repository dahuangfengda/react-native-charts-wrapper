package com.github.wuxudong.rncharts.data;

import com.github.mikephil.charting.data.Entry;

/**
 * Created by zhanghai on 2018/1/19.
 */

public class HYSEntry extends Entry {
    public String date;

    public HYSEntry(float x, float y, Object data) {
        super(x, y , data);
    }

    public HYSEntry(float x, float y) {
        super(x, y);
    }
}
