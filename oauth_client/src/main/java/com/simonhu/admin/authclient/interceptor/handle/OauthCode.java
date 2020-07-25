package com.simonhu.admin.authclient.interceptor.handle;

import com.simonhu.admin.authclient.config.Constants;
import com.simonhu.admin.authclient.config.OAuthClientParameters;
import com.simonhu.admin.authclient.domain.CommonResult;
import com.simonhu.admin.authclient.util.HttpUtil;
import com.simonhu.admin.authclient.util.JsonUtils;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.common.message.types.ResponseType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by admin on 2017/7/5.
 */
@Component
public class OauthCode implements OauthHandleInterface {

    public static final Logger log = LoggerFactory.getLogger(OauthCode.class);


    @Autowired
    private OAuthClientParameters oAuthClientParameters;

    @Override
    public boolean handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        if (httpServletRequest.getSession().getAttribute(Constants.CACHE_USER_INFO) == null) {
            //获取授权回调地址，就是当前请求地址
            String reqUrl = HttpUtil.getReqUrl(httpServletRequest);
            //回调地址需要url转码
//            reqUrl = URLEncoder.encode(reqUrl, "UTF-8");
            OAuthClientRequest request = OAuthClientRequest
                    .authorizationLocation(oAuthClientParameters.getOauth_server_url())
                    .setClientId(oAuthClientParameters.getClient_id())
                    .setRedirectURI(reqUrl)
                    .setScope("#redirect")
                    .setResponseType(ResponseType.CODE.toString())
                    .buildQueryMessage();
            request.getLocationUri();
            httpServletResponse.sendRedirect(request.getLocationUri());
            return false;
        }else{
            String requestPath=httpServletRequest.getServletPath();
            //管理系统中，下面url不拦截。（已经登录后不拦截下面权限）
            if(requestPath.equals("/index")||requestPath.equals("/logout")||requestPath.equals("/userClientMenu")
                    ||requestPath.equals("/user/updatePassWord")){
                return true;
            }
            //已经登录
            //判断请求URL是否被授权
            CommonResult result = (CommonResult) httpServletRequest.getSession().getAttribute(Constants.CACHE_USER_MENU_LIST);
            List<String> menuList = (List<String>) result.getData();
            log.info("请求路径：{}",requestPath);
            log.info("缓存menu：{}", JsonUtils.toJson(menuList));
            for(String menu:menuList){
                if(menu.equals(requestPath)){
                    return true;
                }
            }
            return true;
        }
    }
}
