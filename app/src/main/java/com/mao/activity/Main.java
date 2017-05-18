package com.mao.activity;

import android.app.Activity;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;

import com.example.administrator.maomao_sticker.R;
import com.github.chrisbanes.photoview.CalculateUtil;
import com.github.chrisbanes.photoview.OnDisplayRectChangeListener;
import com.github.chrisbanes.photoview.OnMatrixChangedListener;
import com.github.chrisbanes.photoview.OnScaleChangedListener;
import com.github.chrisbanes.photoview.OnSuperMatrixListener;
import com.github.chrisbanes.photoview.PhotoView;
import com.mao.view.MyView;
import com.mao.view.PhotoCropView;
import com.xiaopo.flying.sticker.BitmapStickerIcon;
import com.xiaopo.flying.sticker.DeleteIconEvent;
import com.xiaopo.flying.sticker.NonIconEvent;
import com.xiaopo.flying.sticker.StickerView;
import com.xiaopo.flying.sticker.TextSticker;
import com.xiaopo.flying.sticker.ZoomIconEvent;

import java.util.Arrays;

/**
 * Created by Administrator on 2017/4/18.
 */

public class Main extends Activity implements PhotoCropView.onLocationListener, OnScaleChangedListener {

    private static final String TAG = "maomao";

    private MyView myView;

    private PhotoView photoView;

    private StickerView stickerView;

    private Matrix matrix;


    private float oldTop = 0;
    private float oldLeft = 0;
    private float oldScale = 1.0f;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        CalculateUtil.getScreenRect(this, true, false);

        photoView = (PhotoView) findViewById(R.id.photo);
        photoView.setImageResource(R.drawable.abc);
        matrix = photoView.getImageMatrix();

        photoView.setOnSuperMatrixListener(new OnSuperMatrixListener() {
            @Override
            public void postTranslate(float dx, float dy) {
                stickerView.postTranslate(dx, dy);
            }

            @Override
            public void postScale(float scaleFactor, float focusX, float focusY) {
                stickerView.postScale(scaleFactor,focusX,focusY);
            }

            @Override
            public void postScale(float scaleFactor) {
                stickerView.postScale(scaleFactor);
            }

            @Override
            public void setScale(float scaleX, float scaleY, float focalX, float focalY) {
                stickerView.setScale(scaleX, scaleY, focalX, focalY);
            }
        });


        photoView.setOnScaleChangeListener(new OnScaleChangedListener() {
            @Override
            public void onScaleChange(Matrix matrix, float scaleFactorX, float focusX, float focusY) {
//                RectF rect=photoView.getDisplayRect();
//                float distanceY = 0;
//                float distanceX = 0;
//                float scale = 1.0f;
//                if (scale != 0) {
//                    scale = photoView.getScale() / oldScale;
//                }
//                if (oldTop != 0) {
//                    distanceY = rect.top - oldTop;
//                }
//                if (oldLeft != 0) {
//                    distanceX = rect.left - oldLeft;
//                }
//                Log.d(TAG, "onMatrixChanged: distance" + distanceX + "==" + distanceY + "++++" + scale);
////                stickerView.test3(scale, distanceX, distanceY);
//
//
//                stickerView.scale(matrix, scaleFactorX, focusX, focusY,distanceX,distanceY);
//
//                oldTop = rect.top;
//                oldLeft = rect.left;
            }
        });

//        photoView.setOnMatrixChangeListener(new OnMatrixChangedListener() {
//            @Override
//            public void onMatrixChanged(RectF rect) {
//                Log.d(TAG, "onMatrixChanged: lefttop" + rect.left + "==" + rect.top);
//                Log.d(TAG, "onMatrixChanged: rightbottom" + rect.right + "==" + rect.bottom);
//                Log.d(TAG, "onMatrixChanged: widthandheight" + rect.width() + "==" + rect.height());
//
//
//                float distanceY = 0;
//                float distanceX = 0;
//                float scale = 1.0f;
//                if (scale != 0) {
//                    scale = photoView.getScale() / oldScale;
//                }
//                if (oldTop != 0) {
//                    distanceY = rect.top - oldTop;
//                }
//                if (oldLeft != 0) {
//                    distanceX = rect.left - oldLeft;
//                }
//                Log.d(TAG, "onMatrixChanged: distance" + distanceX + "==" + distanceY + "++++" + scale);
//                if (scale !=1 ){
//                    return;
//                }
//                stickerView.test3(rect , scale, distanceX, distanceY);
////                stickerView.test4(rect);
//                oldTop = rect.top;
//                oldLeft = rect.left;
//                oldScale = photoView.getScale();
//            }
//        });

//        photoView.setOnSingleFlingListener(new OnSingleFlingListener() {
//            @Override
//            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
//                    stickerView.transfer(e1, e2, velocityX, velocityY);
//                return false;
//            }
//        });

//        photoView.setOnDisplayRectChangeListener(new OnDisplayRectChangeListener() {
//            @Override
//            public void transfer(float[] processValues) {
//                Log.d(TAG, "transfer: processValues=" + processValues[0] + "  " + processValues[1] + "  " + processValues[2] + "  " + processValues[3] + "  " + processValues[4] + "  " + processValues[5] + "  " + processValues[6] + "  " + processValues[7] + "  " + processValues[8]);
//
//            }
////                stickerView.test(processValues);
//        });
        photoView.setOnDisplayRectChangeListener(new OnDisplayRectChangeListener() {
            @Override
            public void transfer(Matrix matrix) {
//                stickerView.test2(matrix);
                RectF rect = new RectF();
                matrix.mapRect(rect);
                Log.d(TAG, "transfer: lefttop" + rect.left + "==" + rect.top);
                Log.d(TAG, "transfer: rightbottom" + rect.right + "==" + rect.bottom);
                Log.d(TAG, "transfer: widthandheight" + rect.width() + "==" + rect.height());


            }
//                stickerView.test(processValues);
        });

//        Drawable drawable = this.getResources().getDrawable(R.mipmap.ic_launcher);
////        BitmapDrawable bitmapDrawable = new BitmapDrawable(drawable);
//        Bitmap bitmap=new BitmapDrawable(drawable);
//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.sticker_transparent_background);
//        myView = new MyView(this,bitmap);
//        myView.addSticker("我编辑的内容");
//        setContentView(myView);

        stickerView = (StickerView) findViewById(R.id.stickerview);
        BitmapStickerIcon deleteIcon = new BitmapStickerIcon(ContextCompat.getDrawable(this,
                com.xiaopo.flying.sticker.R.drawable.sticker_ic_flip_white_18dp),
                BitmapStickerIcon.LEFT_TOP);
        deleteIcon.setIconEvent(new NonIconEvent());

        BitmapStickerIcon zoomIcon = new BitmapStickerIcon(ContextCompat.getDrawable(this,
                com.xiaopo.flying.sticker.R.drawable.sticker_ic_scale_white_18dp),
                BitmapStickerIcon.RIGHT_BOTOM);
        zoomIcon.setIconEvent(new ZoomIconEvent());

        BitmapStickerIcon flipIcon = new BitmapStickerIcon(ContextCompat.getDrawable(this,
                com.xiaopo.flying.sticker.R.drawable.sticker_ic_close_white_18dp),
                BitmapStickerIcon.RIGHT_TOP);
        flipIcon.setIconEvent(new DeleteIconEvent());

        BitmapStickerIcon heartIcon =
                new BitmapStickerIcon(ContextCompat.getDrawable(this, R.drawable.sticker_ic_close_white_18dp),
                        BitmapStickerIcon.LEFT_BOTTOM);
        heartIcon.setIconEvent(new NonIconEvent());
        stickerView.setIcons(Arrays.asList(deleteIcon, zoomIcon, flipIcon, heartIcon));
        addSticker("文字最好不要太多，如果太多看看会发生什么");


    }


    /**
     * 添加stickerview
     *
     * @param text
     */
    public void addSticker(String text) {
        Drawable drawable =
                ContextCompat.getDrawable(this, R.drawable.sticker_transparent_background);
        final TextSticker textSticker = new TextSticker(this);
        textSticker.setDrawable(drawable);
        textSticker.setText(text);
        textSticker.setMaxTextSize(14);
        textSticker.resizeText();
//        DrawableSticker sticker = new DrawableSticker(drawable);

        stickerView.addSticker(textSticker);
    }

    public void add_sticker(View view) {
        addSticker(" ");
    }


    @Override
    public void locationRect(int startX, int startY, int endX, int endY) {
        String a = "sx=" + startX + "ex=" + endX + "sy=" + startY + "ey=" + endY;
        Log.d(TAG, a);
    }


    @Override
    public void onScaleChange(Matrix mtrix, float scaleFactor, float focusX, float focusY) {
//        Log.d(TAG, "onScaleChange: "+scaleFactor);
//        RectF rectF = new RectF();
//        mtrix.mapRect(rectF);
//        Log.d(TAG, "onScaleChange: top"+rectF.top);
//        Log.d(TAG, "onScaleChange: height"+rectF.height());
//        stickerView.scale(mtrix, scaleFactor, focusX, focusY);
    }


}
