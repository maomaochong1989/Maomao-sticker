package com.mao.activity;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2017/6/9.
 */

public class SpUtil {

    private static final String FILE_PRODUCT = "product_file";

    public static boolean saveProduct(Context context ,String key, String value ){
        SharedPreferences sp= context.getSharedPreferences(FILE_PRODUCT,Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.remove(key);
        edit.putString(key, value);
        return edit.commit();
    }

    public static String getProduct(Context context,String key){
        SharedPreferences sp= context.getSharedPreferences(FILE_PRODUCT,Context.MODE_PRIVATE);
        return sp.getString(key,null);
    }

}
