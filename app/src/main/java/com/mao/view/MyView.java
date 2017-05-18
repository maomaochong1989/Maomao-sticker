package com.mao.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.example.administrator.maomao_sticker.R;
import com.mao.view.photoview.MPhotoView;
import com.xiaopo.flying.sticker.BitmapStickerIcon;
import com.xiaopo.flying.sticker.DeleteIconEvent;
import com.xiaopo.flying.sticker.NonIconEvent;
import com.xiaopo.flying.sticker.StickerView;
import com.xiaopo.flying.sticker.TextSticker;
import com.xiaopo.flying.sticker.ZoomIconEvent;

import java.util.Arrays;

/**
 * Created  on 2017/4/17.
 * @author maojinhui
 *
 */

public class MyView extends FrameLayout {

    /**背景图片*/
    private MPhotoView MPhotoView;
    /***/
    private StickerView stickerView;

    /**
     *
     * @param context
     */
    public MyView(Context context, Bitmap bitmap) {
        super(context);
        init(context,bitmap);
    }

    public MyView(Context context, AttributeSet attrs) {
        super(context,attrs);
        BitmapDrawable id = (BitmapDrawable) getAttrs(context, attrs);
        Bitmap bitmap = id.getBitmap();
        init(context,bitmap);


    }

    public MyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        BitmapDrawable id = (BitmapDrawable) getAttrs(context, attrs);
        Bitmap bitmap = id.getBitmap();
        init(context,bitmap);
    }

    /**
     * 得到属性值
     *
     * @param context
     * @param attrs
     */
    private  Drawable getAttrs(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.MyView);
        Drawable attr1 = ta.getDrawable(R.styleable.MyView_img);
        ta.recycle();
        return  attr1;
    }


    /**
     * 初始化基础类
     * @param context
     * @param bitmap 传入一张图片
     */
    private void  init(Context context,Bitmap bitmap){
        LayoutInflater.from(context).inflate(R.layout.myview_root,this);
        MPhotoView = (MPhotoView) findViewById(R.id.myview_photoview);
//        photoView.setScaleType(ImageView.ScaleType.MATRIX);
        MPhotoView.setImageBitmap(bitmap);
//        addView(photoView);



        stickerView= (StickerView) findViewById(R.id.myview_stickerview);
        BitmapStickerIcon deleteIcon = new BitmapStickerIcon(ContextCompat.getDrawable(context,
                com.xiaopo.flying.sticker.R.drawable.sticker_ic_flip_white_18dp),
                BitmapStickerIcon.LEFT_TOP);
        deleteIcon.setIconEvent(new NonIconEvent());

        BitmapStickerIcon zoomIcon = new BitmapStickerIcon(ContextCompat.getDrawable(context,
                com.xiaopo.flying.sticker.R.drawable.sticker_ic_scale_white_18dp),
                BitmapStickerIcon.RIGHT_BOTOM);
        zoomIcon.setIconEvent(new ZoomIconEvent());

        BitmapStickerIcon flipIcon = new BitmapStickerIcon(ContextCompat.getDrawable(context,
                com.xiaopo.flying.sticker.R.drawable.sticker_ic_close_white_18dp),
                BitmapStickerIcon.RIGHT_TOP);
        flipIcon.setIconEvent(new DeleteIconEvent());

        BitmapStickerIcon heartIcon =
                new BitmapStickerIcon(ContextCompat.getDrawable(context, R.drawable.sticker_ic_close_white_18dp),
                        BitmapStickerIcon.LEFT_BOTTOM);
        heartIcon.setIconEvent(new NonIconEvent());
        stickerView.setIcons(Arrays.asList(deleteIcon, zoomIcon, flipIcon, heartIcon));

    }


    protected void onMeasure( int widthMeasureSpec, int heightMeasureSpec) {
        // 计算出所有的childView的宽和高
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        //测量并保存layout的宽高(使用getDefaultSize时，wrap_content和match_perent都是填充屏幕)
        //稍后会重新写这个方法，能达到wrap_content的效果
        setMeasuredDimension( getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec),
                getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec));
    }

    /**
     * 为所有的子控件摆放位置.
     */
    @Override
    protected void onLayout( boolean changed, int left, int top, int right, int bottom) {
        final int count = getChildCount();
        int childMeasureWidth = 0;
        int childMeasureHeight = 0;
        int layoutWidth = 0;    // 容器已经占据的宽度
        int layoutHeight = 0;   // 容器已经占据的宽度
        int maxChildHeight = 0; //一行中子控件最高的高度，用于决定下一行高度应该在目前基础上累加多少
        for(int i = 0; i<count; i++){
            View child = getChildAt(i);
            //注意此处不能使用getWidth和getHeight，这两个方法必须在onLayout执行完，才能正确获取宽高
            childMeasureWidth = child.getMeasuredWidth();
            childMeasureHeight = child.getMeasuredHeight();
            if(layoutWidth<getWidth()){
                //如果一行没有排满，继续往右排列
                left = layoutWidth;
                right = left+childMeasureWidth;
                top = layoutHeight;
                bottom = top+childMeasureHeight;
            } else{
                //排满后换行
                layoutWidth = 0;
                layoutHeight += maxChildHeight;
                maxChildHeight = 0;

                left = layoutWidth;
                right = left+childMeasureWidth;
                top = layoutHeight;
                bottom = top+childMeasureHeight;
            }

            layoutWidth += childMeasureWidth;  //宽度累加
            if(childMeasureHeight>maxChildHeight){
                maxChildHeight = childMeasureHeight;
            }

            //确定子控件的位置，四个参数分别代表（左上右下）点的坐标值
            child.layout(left, top, right, bottom);
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }


    /**
     * 添加stickerview
     * @param text
     */
    public void addSticker(String text){
        Drawable drawable =
                ContextCompat.getDrawable(getContext(), R.mipmap.ic_launcher_round);
        final TextSticker textSticker = new TextSticker(getContext());
        textSticker.setDrawable(drawable);
        textSticker.setText(text);
        textSticker.setMaxTextSize(14);
        textSticker.resizeText();
//        DrawableSticker sticker = new DrawableSticker(drawable);

        stickerView.addSticker(textSticker);
    }





}
