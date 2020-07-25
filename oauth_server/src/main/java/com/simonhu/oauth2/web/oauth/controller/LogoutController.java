package com.simonhu.oauth2.web.oauth.controller;


import com.simonhu.oauth2.common.util.SignUtil;
import com.simonhu.oauth2.web.oauth.service.OAuthService;
import org.apache.commons.lang3.StringUtils;
import org.apache.oltu.oauth2.common.OAuth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yanjun
 * @Description: TODO
 * @date 2018/1/29
 */
@Controller
public class LogoutController {

    @Autowired
    private OAuthService oAuthService;

    @RequestMapping("/logout")
    public Object logout(HttpServletRequest request, HttpServletResponse response,
                         Model model,
                         String grantType, String clientId, String sign,String userId) throws Exception{
        request.getSession().invalidate();

        Map clientInfo = oAuthService.findClientInfo(clientId,null);
        if(clientInfo==null||clientInfo.isEmpty()){
            return  new ResponseEntity("非法请求", HttpStatus.valueOf(HttpServletResponse.SC_BAD_REQUEST));
        }
        String signKey = String.valueOf(clientInfo.get("client_sign_key"));
        Map parMap = new HashMap();
        parMap.put("grantType",grantType);
        parMap.put("clientId",clientId);
        String buildSign = SignUtil.buildSign(parMap,signKey);
        if(!buildSign.equals(sign)){
            return  new ResponseEntity("非法请求", HttpStatus.valueOf(HttpServletResponse.SC_BAD_REQUEST));
        }
        String clientIndexUrl = String.valueOf(clientInfo.get("index_url"));
        if(StringUtils.isNotEmpty(userId)){
            //查询当前用户下所有系统，并退出清除session
            List<String> logoutList= oAuthService.findClientLogoutList(userId,clientId);
            if(logoutList!=null&&logoutList.size()>0){
                for (String logoutUrl:logoutList) {
                    response.sendRedirect(logoutUrl);
                }
            }else {
                //拼接参数
                Map map = new HashMap();
                map.put(OAuth.OAUTH_RESPONSE_TYPE,grantType);
                map.put(OAuth.OAUTH_REDIRECT_URI,clientIndexUrl);
                map.put(OAuth.OAUTH_CLIENT_ID,clientId);
                model.addAttribute("client","测试");
                model.addAttribute("clientParam", map);
                return "login";
            }
            return null;
        }else {
            //拼接参数
            Map map = new HashMap();
            map.put(OAuth.OAUTH_RESPONSE_TYPE,grantType);
            map.put(OAuth.OAUTH_REDIRECT_URI,clientIndexUrl);
            map.put(OAuth.OAUTH_CLIENT_ID,clientId);
            model.addAttribute("client","测试");
            model.addAttribute("clientParam", map);
            return "login";
        }
    }


    @RequestMapping("/test1")
    public Object test1( Model model){
        //拼接参数
        Map map = new HashMap();
        map.put(OAuth.OAUTH_RESPONSE_TYPE,"code");
        map.put(OAuth.OAUTH_REDIRECT_URI,"test13213");
        map.put(OAuth.OAUTH_CLIENT_ID,"clientId");
        model.addAttribute("client","测试");
        model.addAttribute("clientParam", map);
        return "login";
    }

}
