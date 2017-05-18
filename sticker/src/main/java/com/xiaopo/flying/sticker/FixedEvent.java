package com.xiaopo.flying.sticker;

import android.view.MotionEvent;

/**
 * Created by Administrator on 2017/5/18.
 */

public class FixedEvent implements StickerIconEvent {

    @Override
    public void onActionDown(StickerView stickerView, MotionEvent event) {

    }

    @Override
    public void onActionMove(StickerView stickerView, MotionEvent event) {

    }

    @Override
    public void onActionUp(StickerView stickerView, MotionEvent event) {
        Sticker currentSticker = stickerView.getCurrentSticker();
        currentSticker .setRelatedPtohoview(!currentSticker.isRelatedPtohoview());
    }
}
