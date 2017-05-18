package com.github.chrisbanes.photoview;

import android.app.Activity;
import android.content.Context;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by maojinhui on 2017/4/30.
 */


public class CalculateUtil {

    public static float screenWidth = 0;
    public static float screenHeight = 0;
    public static float topZhuobiao = 0;

    /**
     * 计算当前view可用的高度
     *
     * @param context
     * @param hasNotificationBar
     * @param hasActionBar
     * @return
     */
    public static float[] getScreenRect(Activity context, boolean hasNotificationBar, boolean hasActionBar) {
        float[] screenSize = new float[2];
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        int statusHeight = getStatusHeight(context);
        if (hasNotificationBar) {
            if (hasActionBar) {
                height -= statusHeight;
                height -= context.getWindow().findViewById(Window.ID_ANDROID_CONTENT).getHeight();
            } else {
                height -= statusHeight;
            }
        } else {
            if (hasActionBar) {
                height -= context.getWindow().findViewById(Window.ID_ANDROID_CONTENT).getHeight();
            }
        }
        screenSize[0] = width;
        screenSize[1] = height;
        screenWidth = width;
        screenHeight = height;
        return screenSize;

    }

    /**
     * 获取通知栏高度
     * @param context
     * @return
     */
    public static int getStatusHeight(Context context){
        int statusHeight = -1;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height")
                    .get(object).toString());
            statusHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }

}
