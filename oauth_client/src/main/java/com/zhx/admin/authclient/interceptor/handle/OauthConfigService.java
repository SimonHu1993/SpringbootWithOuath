package com.zhx.admin.authclient.interceptor.handle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by admin on 2017/7/5.
 */
@Component
public class OauthConfigService {

    @Autowired
    private OauthCode oauthCode;

    @Autowired
    private OauthAccessToken oauthAccessToken;

    private Set<OauthHandleInterface> set = new LinkedHashSet();

    private boolean isNotInit = true;

    public boolean handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        if (isNotInit) {
            registerOAuth();
        }
        boolean flag = true;
        for (OauthHandleInterface oAuth : set) {
            flag = oAuth.handle(httpServletRequest, httpServletResponse);
            if (!flag) {
                return flag;
            }
        }
        return flag;
    }

    private void registerOAuth() {
        //构建责任链
        if (isNotInit) {
            set.add(oauthAccessToken);
            set.add(oauthCode);
            isNotInit = false;
        }
    }

}
