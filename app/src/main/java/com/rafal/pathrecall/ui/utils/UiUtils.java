package com.rafal.pathrecall.ui.utils;


import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

public class UiUtils {
    public static Point getScreenSize(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size;
    }
}
