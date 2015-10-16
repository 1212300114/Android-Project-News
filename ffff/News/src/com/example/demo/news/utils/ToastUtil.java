package com.example.demo.news.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by jijunjie on 15/10/16.
 */
public class ToastUtil {

    public static void showToast(Context context, String content, boolean isShort) {
        if (isShort) {
            Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, content, Toast.LENGTH_LONG).show();
        }
    }
    public static void  showToast(Context context, String content){
        Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
    }
}
