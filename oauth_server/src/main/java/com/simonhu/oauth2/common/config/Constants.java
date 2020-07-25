package com.simonhu.oauth2.common.config;

public class Constants {
    public static final String RESOURCE_SERVER_NAME = "oauth-server";
    public static final String INVALID_CLIENT_ID = "客户端验证失败，如错误的client_id/client_secret。";
    public static final String INVALID_ACCESS_TOKEN = "accessToken无效或已过期。";
    public static final String INVALID_REDIRECT_URI = "缺少授权成功后的回调地址。";
    public static final String INVALID_AUTH_CODE = "错误的授权码。";
    // 验证accessToken
    public static final String CHECK_ACCESS_CODE_URL = "http://localhost:8080/zetark-oauth2-server/checkAccessToken?accessToken=";

    //缓存到session的用户key
    public static final String CACHE_USER_INFO_KEY = "user_info";


}
