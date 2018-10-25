package com.zhx.oauth2.web.interceptor;

import com.zhx.oauth2.common.domain.CommonResult;
import com.zhx.oauth2.common.util.JsonUtils;
import com.zhx.oauth2.common.util.SignUtil;
import com.zhx.oauth2.web.log.service.LogService;
import com.zhx.oauth2.web.oauth.service.OAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 拦截资源请求服务
 * Created by admin on 2017/7/5.
 */
@Component
public class SourceServerInterceptor implements HandlerInterceptor {

    @Autowired
    private OAuthService oAuthService;
    @Autowired
    private LogService logService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        String sign = "";
        Map paramMap = new HashMap();
        //获取提交参数
        StringBuilder sb = new StringBuilder();
        if (request.getParameterMap() == null || request.getParameterMap().size() == 0) {
            outPutResult(request, response, "009", "系统错误，请检查您的网络(E312)", null);
            return false;
        }
        Iterator<Map.Entry<String, String[]>> entries = request.getParameterMap().entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<String, String[]> entry = entries.next();
            String key = entry.getKey();
            if (key.equals("sign")) {
                for (String val : entry.getValue()) {
                    sign = val;
                }
            }
            String value = entry.getValue()[0];
            sb.append(key + "=" + value + ",");
            paramMap.put(key, value);
        }

        //验证签名
        String accessToken = String.valueOf(paramMap.get("accessToken"));
        String clientId = oAuthService.getCacheInfoByAccessToken(accessToken, 2);
        if(clientId==null){
            outPutResult(request, response, "889", "accessToken已过期", null);
            return false;
        }
        Map clientInfo = oAuthService.findClientInfo(clientId,null);
        String signKey = String.valueOf(clientInfo.get("client_sign_key"));
        String buildSign = SignUtil.buildServerSign(paramMap,signKey);
        if(!sign.equals(buildSign)){
            outPutResult(request, response, "888", "签名不一致", null);
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }

    /**
     * 输出结果
     *
     * @param request
     * @param response
     * @param returncode
     * @param msg
     * @param info
     */
    private void outPutResult(HttpServletRequest request, HttpServletResponse response, String returncode, String msg, String info) {
        Map resMap = null;
        response.setCharacterEncoding("UTF-8");
        PrintWriter pw = null;
        try {
            pw = response.getWriter();
        } catch (IOException e) {
            e.printStackTrace();
        }
        pw.write(JsonUtils.toJsonNf(CommonResult.result(returncode,msg)));
        pw.close();
        //logService.saveClientLog(request,JsonUtils.toJsonNf(CommonResult.result(returncode,msg)),"oauth_server");
    }
}
