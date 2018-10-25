package com.zhx.oauth2.common.shortmessage.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhx.oauth2.common.config.SmsConfig;
import com.zhx.oauth2.common.shortmessage.mapper.ShortMessageMapper;
import com.zhx.oauth2.common.util.HttpUtils;
import com.zhx.oauth2.common.util.Sha1Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class ShortMessageService {


    @Autowired
    private SmsConfig smsConfig;
    @Autowired
    private ShortMessageMapper shortMessageMapper;


    /**
     * 获取验证码（发送短信）  验证码
     *
     * @param phone    手机号
     * @param operType 操作类型 100：登录   200：兑换糯米券
     * @return
     */
    public Map sendSMSPhoneCode(String phone, String operType) {
        Map resMap = null;
        //短信模板ID
        Map map = new HashMap();
        map.put("phoneno", phone);
        String phonecode = getCode();
        String content = "您的验证码为：" + phonecode + "，请尽快验证！";
        map.put("content", content);

        map.put("merchantid", smsConfig.getMerchantid());
        map.put("v", smsConfig.getVersion());
        String encodeparam = phone + content + smsConfig.getMerchantid() + smsConfig.getVersion();
        String sign = "";
        try {
            encodeparam = URLEncoder.encode(encodeparam, "utf-8");
            sign = Sha1Util.encodeSha1(encodeparam, smsConfig.getMerchantKey());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        map.put("sign", sign);
        resMap = sendSMS(map);
        //保存短信
        Map parMap = new HashMap();
        parMap.put("phone", phone);
        parMap.put("content", phonecode);
        parMap.put("message_type", operType);
        long now = new Date().getTime();
        long effective = now + (smsConfig.getEffectiveTime() * 60 * 1000);
        parMap.put("sendtime", now);
        parMap.put("endtime", effective);
        saveMessage(resMap, parMap);
        return resMap;
    }
    /**
     * 返回6位随机验证码
     *
     * @return
     */
    private String getCode() {
        long round = Math.round(Math.random() * 899999 + 100000);
        return String.valueOf(round);
    }
    /**
     * 发送
     *
     * @param map
     * @return
     */
    private Map sendSMS(Map map) {
        Map resMap = null;
        //发送验证码
        String resultStr = HttpUtils.doPost(smsConfig.getSmsUrl(), map, null, 10000);
        if (resultStr != null && !"".equals(resultStr)) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                resMap = objectMapper.readValue(resultStr, Map.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return resMap;
    }
    /**
     * 保存到数据库
     *
     * @param resMap 短信接口返回值
     * @param parmap 需要加入数据库的信息
     */
    private void saveMessage(Map resMap, Map parmap) {
        if (resMap != null) {
            if ("000".equals(String.valueOf(resMap.get("returncode")))) {
                shortMessageMapper.updatePhoneMessage(parmap);
                shortMessageMapper.insertPhoneMessage(parmap);
            }
        }
    }
    /**
     * 验证短信验证码。  (返回设置无效的短信数量，如果更新数量为0，则表示验证码错误);
     *
     * @param phone 手机号
     * @return
     */
    public int updatePhoneMessage(String phone, String phoceCode) {
        Map parmap = new HashMap();
        parmap.put("phone", phone);
        parmap.put("phoceCode", phoceCode);
        parmap.put("nowtime", new Date().getTime());
        //为IPHONE 客户端  APP store 审核帐号  （测试 2015-12-03 12：10：00）
        if ("15210218167".equals(phone)) {
            return 1;
        }
        if (smsConfig.isValidateSMSCode()) {
            return shortMessageMapper.updatePhoneMessage(parmap);
        } else {
            return 1;
        }
    }

}
