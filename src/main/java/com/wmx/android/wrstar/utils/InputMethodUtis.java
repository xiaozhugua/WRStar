/*
 * -----------------------------------------------------------------
 * Copyright (C) 2015, by Peanut Run, Shenzhen, All rights reserved.
 * -----------------------------------------------------------------
 *
 * File: InputMethodUtis
 * Author: Mark
 * Version: 1.0
 * Create: 2015/10/11 0011
 *
 * Changes (from 2015/10/11 0011)
 * -----------------------------------------------------------------
 * 2015/10/11 0011 : 创建 InputMethodUtis (Mark);
 * -----------------------------------------------------------------
 */
package com.wmx.android.wrstar.utils;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * 输入法工具类.
 */
public class InputMethodUtis {

    /**
     * 隐藏输入法.
     */
    public static void hideInputDialog(Context context) {
        // 隐藏输入法
        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        // 显示或者隐藏输入法
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    //显示虚拟键盘
    public static void ShowKeyboard(View v)
    {
        InputMethodManager imm = ( InputMethodManager ) v.getContext( ).getSystemService(Context.INPUT_METHOD_SERVICE);

        imm.showSoftInput(v, InputMethodManager.SHOW_FORCED);

    }
}
