package com.android.juzbao.utils;

import android.app.Activity;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * Created by admin on 2017/3/22.
 */

public class Util {

    /**
     * 切换软键盘的状态
     */
    public static void toggleSoftKeyboardState(Context context) {
        ((InputMethodManager) context.getSystemService(
                Context.INPUT_METHOD_SERVICE)).toggleSoftInput(
                InputMethodManager.SHOW_IMPLICIT,
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 关闭软键盘
     */
    public static void closeSoftKeyboard(Activity activity, EditText editText) {
        InputMethodManager inputMethodManager
                = (InputMethodManager) activity.getSystemService(
                Context.INPUT_METHOD_SERVICE);
        //如果软键盘已经开启
        if (inputMethodManager.isActive()) {
            inputMethodManager.hideSoftInputFromWindow(
                    activity.getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }else {
            inputMethodManager.showSoftInput(editText,
                    InputMethodManager.SHOW_IMPLICIT);
        }
    }
}
