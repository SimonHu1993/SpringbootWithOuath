package com.zhx.oauth2.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author yanjun
 * @Description: TODO
 * @date 2018/1/19
 */
@Component
@ConfigurationProperties(prefix = "site")
public class SiteConfig {

    private String pwdSalt;

    public String getPwdSalt() {
        return pwdSalt;
    }

    public void setPwdSalt(String pwdSalt) {
        this.pwdSalt = pwdSalt;
    }



}
