package com.zhx.admin.authclient.interceptor.handle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by admin on 2017/7/5.
 */
public interface OauthHandleInterface {
    public boolean handle(HttpServletRequest request, HttpServletResponse response) throws Exception;
}
