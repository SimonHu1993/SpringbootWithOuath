package com.simonhu.admin.authclient.config;

public class Constants {
    public static final String RESOURCE_SERVER_NAME = "oauth-server";
    public static final String INVALID_CLIENT_ID = "客户端验证失败，如错误的client_id/client_secret。";
    public static final String INVALID_ACCESS_TOKEN = "accessToken无效或已过期。";
    public static final String INVALID_REDIRECT_URI = "缺少授权成功后的回调地址。";
    public static final String INVALID_AUTH_CODE = "错误的授权码。";

    public static final String CACHE_ACCESS_TOKEN = "access_token";
    public static final String CACHE_USER_INFO = "user_info";
    public static final String CACHE_USER_MENU_LIST = "user_menu_list";
}
