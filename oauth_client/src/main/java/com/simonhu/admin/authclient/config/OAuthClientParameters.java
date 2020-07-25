package com.simonhu.admin.authclient.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * oauth客户端参数
 * Created by admin on 2017/7/5.
 */
@Component
@ConfigurationProperties(prefix = "simonhuAdmin.oauthClient")
public class OAuthClientParameters {

    /**
     * 客户端ID
     */
    private String client_id;
    /**
     * 客户端密钥
     */
    private String client_secret;
    /**
     * 获取服务端授权码地址
     */
    private String oauth_server_url;
    /**
     * 获取access_token地址
     */
    private String oauth_access_token_url;
    /**
     * 刷新access_token地址
     */
    private String oauth_refresh_access_token_url;
    /**
     * 获取用户信息地址
     */
    private String oauth_user_info_url;
    /**
     * 获取用户权限列表
     */
    private String oauth_user_menu_list_url;
    /**
     * 客户端签名key
     */
    private String oauth_client_sign_key;

    /**
     * 服务端退出地址
     */
    private String oauth_client_logout_url;




    public String getOauth_client_sign_key() {
        return oauth_client_sign_key;
    }

    public void setOauth_client_sign_key(String oauth_client_sign_key) {
        this.oauth_client_sign_key = oauth_client_sign_key;
    }

    public String getOauth_refresh_access_token_url() {
        return oauth_refresh_access_token_url;
    }

    public void setOauth_refresh_access_token_url(String oauth_refresh_access_token_url) {
        this.oauth_refresh_access_token_url = oauth_refresh_access_token_url;
    }

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getClient_secret() {
        return client_secret;
    }

    public void setClient_secret(String client_secret) {
        this.client_secret = client_secret;
    }

    public String getOauth_server_url() {
        return oauth_server_url;
    }

    public void setOauth_server_url(String oauth_server_url) {
        this.oauth_server_url = oauth_server_url;
    }

    public String getOauth_access_token_url() {
        return oauth_access_token_url;
    }

    public void setOauth_access_token_url(String oauth_access_token_url) {
        this.oauth_access_token_url = oauth_access_token_url;
    }

    public String getOauth_user_info_url() {
        return oauth_user_info_url;
    }

    public void setOauth_user_info_url(String oauth_user_info_url) {
        this.oauth_user_info_url = oauth_user_info_url;
    }

    public String getOauth_client_logout_url() {
        return oauth_client_logout_url;
    }

    public void setOauth_client_logout_url(String oauth_client_logout_url) {
        this.oauth_client_logout_url = oauth_client_logout_url;
    }

    public String getOauth_user_menu_list_url() {
        return oauth_user_menu_list_url;
    }

    public void setOauth_user_menu_list_url(String oauth_user_menu_list_url) {
        this.oauth_user_menu_list_url = oauth_user_menu_list_url;
    }
}
