package com.simonhu.oauth2.web.test;

import com.simonhu.oauth2.common.util.Sha1Util;

/**
 * @author yanjun
 * @Description: TODO
 * @date 2018/1/30
 */
public class TestCreatePwd {
    public static void main(String[] args) {
        System.out.println(Sha1Util.encodeSha256("123456","#$%RFVtgb^&*("));
        System.out.println("");
    }
}
