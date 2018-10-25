package com.zhx.admin.authclient.interceptor.handle;

import com.zhx.admin.authclient.config.Constants;
import com.zhx.admin.authclient.config.OAuthClientParameters;
import com.zhx.admin.authclient.domain.CommonResult;
import com.zhx.admin.authclient.service.OauthService;
import com.zhx.admin.authclient.util.HttpUtil;
import com.zhx.admin.authclient.util.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.oltu.oauth2.client.OAuthClient;
import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.response.OAuthAccessTokenResponse;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.Enumeration;

/**
 * Created by admin on 2017/7/5.
 */
@Component
public class OauthAccessToken implements OauthHandleInterface {

    public static final Logger log = LoggerFactory.getLogger(OauthAccessToken.class);

    @Autowired
    private OAuthClientParameters oAuthClientParameters;

    @Autowired
    private OauthService oauthService;

    @Override
    public boolean handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String code = request.getParameter("code");
        if (StringUtils.isNotEmpty(code)) {
            //获取授权回调地址，就是当前请求地址
            String reqUrl = HttpUtil.getReqUrl(request);
            //回调地址需要url转码
            reqUrl = URLEncoder.encode(reqUrl, "UTF-8");
            OAuthClientRequest r = OAuthClientRequest
                    .tokenLocation(oAuthClientParameters.getOauth_access_token_url())
                    .setGrantType(GrantType.AUTHORIZATION_CODE)
                    .setClientId(oAuthClientParameters.getClient_id())
                    .setClientSecret(oAuthClientParameters.getClient_secret())
                    .setRedirectURI(reqUrl)
                    .setCode(code)
                    .buildQueryMessage();
            OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());
            OAuthAccessTokenResponse oAuthResponse = oAuthClient.accessToken(r);
            log.info(JsonUtils.toJson(oAuthResponse));
            //缓存access_token
            String accessToken = oAuthResponse.getOAuthToken().getAccessToken();
            request.getSession().setAttribute(Constants.CACHE_ACCESS_TOKEN,accessToken);
            //缓存用户信息到session
            CommonResult userInfo = oauthService.findUserInfo(accessToken);
            request.getSession().setAttribute(Constants.CACHE_USER_INFO,userInfo);
            //缓存用户权限信息到session
            CommonResult menuList = oauthService.findUserMenuList(accessToken);
            request.getSession().setAttribute(Constants.CACHE_USER_MENU_LIST,menuList);

            //======将请求中的code去除   auth服务端返回时候会带有code参数，不清除的话，会造成每次请都去获取code授权。
            String url = request.getRequestURL().toString();
            String params = "";
            Enumeration<String> paramKeys = request.getParameterNames();
            while (paramKeys.hasMoreElements()) {
                String key = paramKeys.nextElement();
                if(!"null".equals(key) &&!"code".equals(key)){
                    String[] value = request.getParameterValues(key);
                    params = params + key + "=" + StringUtils.join(value,",") + "&" ;
                }
            }
            if(StringUtils.isNotEmpty(params)){
                params = params.substring(0, params.length()-1);
                url = url + "?" + params;
            }
            response.sendRedirect(url);
        }
        return true;
    }
}
