package com.zhx.oauth2.common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author SimonHu
 * @Description:常用参数校验
 * @Created on 2018/3/19 16:39
 */
public class VerificationUtil {

    /**
     * 正则表达式：验证手机号
     */
    public static final String REGEX_MOBILE = "^((16[0-9])|(17[0-9])|(14[0-9])|(13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$";

    /**
     * 正则表达式：验证邮箱
     */
    public static final String REGEX_EMAIL = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";

    /**
     * 正则表达式：验证汉字
     */
    public static final String REGEX_CHINESE = "^[\u4e00-\u9fa5],{0,}$";

    /**
     * 校验纳税人识别号：大于等于15位数字和大写字母组合
     */
    public static final String REGEX_TAX_CODE = "^[0-9A-Z]{15,}$";


    /**
     * 校验手机号
     *
     * @param mobile
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isMobile(String mobile) {
        return Pattern.matches(REGEX_MOBILE, mobile);
    }

    /**
     * 校验邮箱
     *
     * @param email
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isEmail(String email) {
        return Pattern.matches(REGEX_EMAIL, email);
    }

    /**
     * 校验汉字
     *
     * @param chinese
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isChinese(String chinese) {
        return Pattern.matches(REGEX_CHINESE, chinese);
    }

    /**
     * 校验纳税人识别号
     *
     * @param taxCode
     * @return
     */
    public static boolean isTAXCODE(String taxCode) { return Pattern.matches(REGEX_TAX_CODE, taxCode);}

    /**
     * 是否是正整数
     * @param value
     */
    public static boolean isIntege1(String value){
        Pattern p=null;//正则表达式
        Matcher m=null;//操作符表达式
        boolean b=false;
        p=p.compile("^[1-9]\\d*$");
        m=p.matcher(value);
        b=m.matches();
        return b;
    }

    public static void main(String[] args) {
        String username = "154893431";
        System.out.println(VerificationUtil.isTAXCODE(username));
    }
}
