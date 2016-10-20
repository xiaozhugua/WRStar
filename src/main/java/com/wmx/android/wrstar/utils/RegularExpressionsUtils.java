package com.wmx.android.wrstar.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则表达式工具类 .
 */
public class RegularExpressionsUtils {

    /**
     * 校验验证码.
     *
     * @param authCode
     *
     * @return
     */
    public static boolean checkAuthCode(String authCode) {

        Pattern p = Pattern.compile("[\\d]{4}$");
        Matcher m = p.matcher(authCode);

        return m.matches();
    }

    /**
     * 校验手机号, 只需保证手机号为11位整数，手机号的合法性会在后台校验.
     *
     * @param phoneNum
     *
     * @return
     */
    public static boolean checkPhoneNum(String phoneNum){
        String str = "[\\d]{11}$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(phoneNum);

        return m.matches();
    }

    /**
     * 校验密码.
     *
     * @param password
     * @return
     */
    public static boolean checkPassword(String password){
        String str = ".{6,20}$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(password);

        return m.matches();
    }
}
