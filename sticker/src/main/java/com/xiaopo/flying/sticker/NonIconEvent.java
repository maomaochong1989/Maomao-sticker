package com.xiaopo.flying.sticker;

import android.util.Log;
import android.view.MotionEvent;

/**
 * Created by Administrator on 2017/4/20.
 */

public class NonIconEvent implements StickerIconEvent {
    @Override
    public void onActionDown(StickerView stickerView, MotionEvent event) {

    }

    @Override
    public void onActionMove(StickerView stickerView, MotionEvent event) {

    }

    @Override
    public void onActionUp(StickerView stickerView, MotionEvent event) {
        Log.d("maomao","It do nothing");
    }
}
