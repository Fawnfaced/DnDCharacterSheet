package com.example.dndcharactersheet.util;

public class ClickHelper {
    private static long lastClickTime = 0;

    public static boolean isSingleClick(){
        long currentClickTime = System.currentTimeMillis();
        if (currentClickTime - lastClickTime< Constants.CLICK_INTERVAL){
            return false;
        }

        lastClickTime = currentClickTime;

        return true;
    }
}
