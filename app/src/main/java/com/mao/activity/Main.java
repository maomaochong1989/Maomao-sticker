package com.mao.activity;

import android.app.Activity;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;

import com.example.administrator.maomao_sticker.R;
import com.github.chrisbanes.photoview.CalculateUtil;
import com.github.chrisbanes.photoview.OnSuperMatrixListener;
import com.github.chrisbanes.photoview.PhotoView;
import com.mao.model.Product;
import com.mao.view.Stick;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.xiaopo.flying.sticker.BitmapStickerIcon;
import com.xiaopo.flying.sticker.DeleteIconEvent;
import com.xiaopo.flying.sticker.FixedEvent;
import com.xiaopo.flying.sticker.NonIconEvent;
import com.xiaopo.flying.sticker.Sticker;
import com.xiaopo.flying.sticker.StickerView;
import com.xiaopo.flying.sticker.TextSticker;
import com.xiaopo.flying.sticker.ZoomIconEvent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2017/4/18.
 */

public class Main extends Activity {

    private static final String TAG = "maomao";


    private PhotoView photoView;

    private StickerView stickerView;

    private Matrix matrix = null;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        CalculateUtil.getScreenRect(this, true, false);

        photoView = (PhotoView) findViewById(R.id.photo);
//        photoView.setImageResource(R.drawable.abc);

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
        ImageLoader loader=ImageLoader.getInstance();
        loader.init(config);
        loader.displayImage("http://img0.imgtn.bdimg.com/it/u=1431766069,3244964907&fm=26&gp=0.jpg",photoView);


        matrix = photoView.getImageMatrix();

        photoView.setOnSuperMatrixListener(new OnSuperMatrixListener() {
            @Override
            public void postTranslate(float dx, float dy) {
                stickerView.postTranslate(dx, dy);
            }

            @Override
            public void postScale(float scaleFactor, float focusX, float focusY) {
                stickerView.postScale(scaleFactor, focusX, focusY);
                Log.d(TAG, "postScale---: " + scaleFactor);
            }

            @Override
            public void postScale(float scaleFactor) {
                stickerView.postScale(scaleFactor);
                Log.d(TAG, "postScale: " + scaleFactor);
            }

            @Override
            public void setScale(float scaleX, float scaleY, float focalX, float focalY) {
                stickerView.setScale(scaleX, scaleY, focalX, focalY);
            }
        });






//        Drawable drawable = this.getResources().getDrawable(R.mipmap.ic_launcher);
//        BitmapDrawable bitmapDrawable = new BitmapDrawable(drawable);
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
                new BitmapStickerIcon(ContextCompat.getDrawable(this, R.mipmap.left_bottom_heart_27),
                        BitmapStickerIcon.LEFT_BOTTOM);
        heartIcon.setIconEvent(new FixedEvent());
        stickerView.setIcons(Arrays.asList(deleteIcon, zoomIcon, flipIcon, heartIcon));


        findViewById(R.id.saveinfo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Product product = new Product();
                product.setId(1);
                product.setKey("test");
                product.setPhoto("http://img0.imgtn.bdimg.com/it/u=1431766069,3244964907&fm=26&gp=0.jpg");
                Matrix matrix = new Matrix();
                photoView.getAttacher().getSuppMatrix(matrix);
                product.setPhotoMatrix(matrix);
                //保存图片的放大比例
                save(product,photoView,stickerView);
            }
        });


        findViewById(R.id.displayinfo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pString = SpUtil.getProduct(Main.this,"test");
                if (pString == null){
                    addSticker("文字最好不要太多，如果太多看看会发生什么");
                }else {
                    Log.d(TAG, "onCreate: pString"+pString);
                    try {
                        JSONObject obj = new JSONObject(pString);
                        JSONArray array = obj.optJSONArray("photomatrix");
                        float[] values = new float[9];
                        for (int i =0;i<array.length();i++){
                            values[i]=(float) array.getDouble(i);
                        }
                        Matrix matrix = new Matrix();
                        matrix.setValues(values);
                        photoView.getAttacher().setSupperMatrix(matrix);
                        obj.optJSONObject("stickers");
                        JSONArray stickerArray = obj.optJSONArray("stickers");
                        for (int i = 0;i<stickerArray.length();i++){
                            JSONObject object = stickerArray.optJSONObject(i);
                            String text = object.optString("text");
                            JSONArray arr = object.optJSONArray("val");
                            Matrix stickerMatrix=new Matrix();
                            float[] val = new float[9];
                            for (int j=0;j<arr.length();j++){
                                val[j]=(float)arr.getDouble(j);
                            }
                            stickerMatrix.setValues(val);
                            addSticker(text,stickerMatrix);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        findViewById(R.id.removestickers).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               stickerView.removeAllStickers();
            }
        });



        String pString = SpUtil.getProduct(this,"test");
        if (pString == null){
            addSticker("文字最好不要太多，如果太多看看会发生什么");
        }else {
            Log.d(TAG, "onCreate拿到的值: "+pString);
        }



    }

    public void save(Product product, PhotoView photoView, StickerView stickerView) {
        String key = product.getKey();
        Matrix photoMatrix = product.getPhotoMatrix();
        float[] values = new float[9];
        photoMatrix.getValues(values);
        JSONObject object = new JSONObject();
        try {
            object.put("key",key);
            JSONArray array = new JSONArray();
            for (int i=0;i<9;i++){
                array.put(values[i]);
            }
            object.put("photomatrix",array);
            JSONArray stickerArray = new JSONArray();
            List<Sticker> stickers = stickerView.getStickers();
            for (int i = 0 ; i < stickers.size() ; i++){
                Sticker sticker = stickers.get(i);
                Matrix matrix = sticker.getMatrix();
                JSONObject stickerobj = new JSONObject();
                stickerobj.put("text",((TextSticker)sticker).getText());
                JSONArray stick = new JSONArray();
                float[] val = new float[9];
                matrix.getValues(val);
                for (int j=0;j<9;j++){
                    stick.put(val[j]);
                }
                stickerobj.put("val",stick);
                stickerArray.put(stickerobj);
            }
            object.put("stickers",stickerArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }



        SpUtil.saveProduct(this,key,object.toString());
    }

    /**
     * 添加stickerview
     *
     * @param text 添加的sticker的文字
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

    public void addSticker(String text,Matrix matrix) {
        Drawable drawable =
                ContextCompat.getDrawable(this, R.drawable.sticker_transparent_background);
        final TextSticker textSticker = new TextSticker(this);
        textSticker.setDrawable(drawable);
        textSticker.setText(text);
        textSticker.setMaxTextSize(14);
        textSticker.resizeText();
//        DrawableSticker sticker = new DrawableSticker(drawable);

        stickerView.addSticker(textSticker,matrix);
    }




    public void add_sticker(View view) {
        addSticker(" ");
    }


}
