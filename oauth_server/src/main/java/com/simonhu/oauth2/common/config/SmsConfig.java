package com.simonhu.oauth2.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by admin on 2017/12/20.
 */
@Component
@ConfigurationProperties(prefix = "smsConfig")
public class SmsConfig {
    /**
     * 发送短信服务URL
     */
    private String smsUrl;
    /**
     * 短信商户号
     */
    private String merchantid;
    /**
     * 短信签名key
     */
    private String merchantKey;
    /**
     * 短信验证码有效期  分钟
     */
    private int effectiveTime;
    /**
     * 短信验证码模版
     */
    private String tempId;
    /**
     * 版本 默认0.1
     */
    private String version ;
    /**
     * 是否实际发送短信、验证短信
     */
    private boolean validateSMSCode;


    public String getSmsUrl() {
        return smsUrl;
    }

    public void setSmsUrl(String smsUrl) {
        this.smsUrl = smsUrl;
    }

    public String getMerchantid() {
        return merchantid;
    }

    public void setMerchantid(String merchantid) {
        this.merchantid = merchantid;
    }

    public String getMerchantKey() {
        return merchantKey;
    }

    public void setMerchantKey(String merchantKey) {
        this.merchantKey = merchantKey;
    }

    public int getEffectiveTime() {
        return effectiveTime;
    }

    public void setEffectiveTime(int effectiveTime) {
        this.effectiveTime = effectiveTime;
    }

    public String getTempId() {
        return tempId;
    }

    public void setTempId(String tempId) {
        this.tempId = tempId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public boolean isValidateSMSCode() {
        return validateSMSCode;
    }

    public void setValidateSMSCode(boolean validateSMSCode) {
        this.validateSMSCode = validateSMSCode;
    }

}
