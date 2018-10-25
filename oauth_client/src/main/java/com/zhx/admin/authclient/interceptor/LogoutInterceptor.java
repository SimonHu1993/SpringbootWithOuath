package com.zhx.admin.authclient.interceptor;

import com.zhx.admin.authclient.config.Constants;
import com.zhx.admin.authclient.config.OAuthClientParameters;
import com.zhx.admin.authclient.domain.CommonResult;
import com.zhx.admin.authclient.util.JsonUtils;
import com.zhx.admin.authclient.util.SignUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 退出登录
 * @author yanjun
 * @Description: TODO
 * @date 2018/1/29
 */
@Component
public class LogoutInterceptor implements HandlerInterceptor {

    @Autowired
    private OAuthClientParameters oAuthClientParameters;

    private static final Logger log = LoggerFactory.getLogger(LogoutInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        CommonResult result = (CommonResult) httpServletRequest.getSession().getAttribute(Constants.CACHE_USER_INFO);
        log.info("退出登录{}", JsonUtils.toJson(result));
        httpServletRequest.getSession().invalidate();
        String userId = "";
        if(result!=null && result.isSuc()){
            Map map = (Map)result.getData();
            Map userInfo = (Map)map.get(Constants.CACHE_USER_INFO);
            userId = String.valueOf(userInfo.get("user_id"));
        }


        Map parMap = new HashMap();
        parMap.put("grantType","code");
        parMap.put("clientId",oAuthClientParameters.getClient_id());
        String sign = SignUtil.buildSign(parMap, oAuthClientParameters.getOauth_client_sign_key());
        parMap.put("sign",sign);
        parMap.put("userId",userId);
        String logoutUrl=oAuthClientParameters.getOauth_client_logout_url();
        //拼装参数
        String param = "";
        Iterator<Map.Entry<String, Object>> entries = parMap.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<String, Object> entry = entries.next();
            String key = entry.getKey();
            String value = String.valueOf(entry.getValue());
            param= param+ key+"="+value+"&";
        }
        param = param.substring(0,param.length()-1);
        logoutUrl= logoutUrl+"?"+param;
        //转码
        httpServletResponse.sendRedirect(logoutUrl);
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
