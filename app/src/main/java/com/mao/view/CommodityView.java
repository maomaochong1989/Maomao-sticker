package com.mao.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Administrator on 2017/4/19.
 */

public class CommodityView extends View {
    public CommodityView(Context context) {
        super(context);
    }

    public CommodityView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CommodityView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(21)
    public CommodityView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }




}
