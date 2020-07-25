package com.simonhu.oauth2.web.oauth.controller;

import com.simonhu.oauth2.common.config.Constants;
import com.simonhu.oauth2.common.domain.CommonResult;
import com.simonhu.oauth2.common.domain.Status;
import com.simonhu.oauth2.common.util.JsonUtils;
import com.simonhu.oauth2.web.oauth.service.OAuthService;
import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.oltu.oauth2.as.request.OAuthAuthzRequest;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.error.OAuthError;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.oltu.oauth2.common.message.types.ResponseType;
import org.apache.oltu.oauth2.common.utils.OAuthUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yj on 2017-07-04.
 */
@Controller
public class AuthorizeController {

    @Autowired
    private OAuthService oAuthService;

    /**
     * 授权
     * 备注：
     *      用户在客户端A登录后，授权服务端记录用户id在session ，记录用户登录有效期。
     *      用户点击客户端B时，因为是同一个浏览器，所以授权服务端也默认用户已经登录，直接生成新的access_token
     *      access_tonke对应一个user_id
     *
     * @param model
     * @param request
     * @return
     * @throws URISyntaxException
     * @throws OAuthSystemException
     */
    @RequestMapping(value = "/authorize")
    public Object authorize(
            Model model,
            HttpServletRequest request)
            throws URISyntaxException, OAuthSystemException {
        try {
            //构建OAuth 授权请求
            OAuthAuthzRequest oauthRequest = new OAuthAuthzRequest(request);
            //检查传入的客户端id是否正确
            if (!oAuthService.checkClientId(oauthRequest.getClientId(),null)) {
                OAuthResponse response =
                        OAuthASResponse.errorResponse(HttpServletResponse.SC_BAD_REQUEST)
                                .setError(OAuthError.TokenResponse.INVALID_CLIENT)
                                .setErrorDescription(Constants.INVALID_CLIENT_ID)
                                .buildJSONMessage();
                return new ResponseEntity(response.getBody(), HttpStatus.valueOf(response.getResponseStatus()));
            }
            //如果用户没有登录，跳转到登陆页面
            CommonResult<String>result = login(request);
            if(!result.isSuc()) {
                Map map = new HashMap();
                map.put(OAuth.OAUTH_RESPONSE_TYPE,oauthRequest.getParam(OAuth.OAUTH_RESPONSE_TYPE));
                map.put(OAuth.OAUTH_REDIRECT_URI,oauthRequest.getParam(OAuth.OAUTH_REDIRECT_URI));
                map.put(OAuth.OAUTH_CLIENT_ID,oauthRequest.getClientId());
                map.put("errorMsg",result.getMsg());
                model.addAttribute("client","测试");
                model.addAttribute("clientParam", map);
                return "login";
            }
            //生成授权码
            String authorizationCode = null;
            //responseType目前仅支持CODE，另外还有TOKEN
            String responseType = oauthRequest.getParam(OAuth.OAUTH_RESPONSE_TYPE);
            if (responseType.equals(ResponseType.CODE.toString())) {
                OAuthIssuerImpl oauthIssuerImpl = new OAuthIssuerImpl(new MD5Generator());
                authorizationCode = oauthIssuerImpl.authorizationCode();
                oAuthService.addAuthCode(authorizationCode, result.getData());
            }
            //进行OAuth响应构建
            OAuthASResponse.OAuthAuthorizationResponseBuilder builder =
                    OAuthASResponse.authorizationResponse(request, HttpServletResponse.SC_FOUND);
            //设置授权码
            builder.setCode(authorizationCode);
            //得到到客户端重定向地址
            String redirectURI = oauthRequest.getParam(OAuth.OAUTH_REDIRECT_URI);
            //构建响应
            OAuthResponse response = builder.location(redirectURI).buildQueryMessage();

            //根据OAuthResponse返回ResponseEntity响应
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(new URI(response.getLocationUri()));
            return new ResponseEntity(headers, HttpStatus.valueOf(response.getResponseStatus()));
        } catch (OAuthProblemException e) {
            //出错处理
            String redirectUri = e.getRedirectUri();
            if (OAuthUtils.isEmpty(redirectUri)) {
                //告诉客户端没有传入redirectUri直接报错
                HttpHeaders responseHeaders = new HttpHeaders();
                responseHeaders.add("Content-Type", "application/json; charset=utf-8");
                Status status = new Status();
                status.setCode(HttpStatus.NOT_FOUND.value());
                status.setMsg(Constants.INVALID_REDIRECT_URI);
                return new ResponseEntity(JsonUtils.toJson(status), responseHeaders ,HttpStatus.NOT_FOUND);
            }
            //返回错误消息（如?error=）
            final OAuthResponse response =
                    OAuthASResponse.errorResponse(HttpServletResponse.SC_FOUND)
                            .error(e).location(redirectUri).buildQueryMessage();
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(new URI(response.getLocationUri()));
            return new ResponseEntity(headers, HttpStatus.valueOf(response.getResponseStatus()));
        }
    }

    /**
     * 验证登录，并缓存用户信息在session中
     * @param request
     * @return
     */
    private CommonResult<String> login(HttpServletRequest request){
        if (request.getSession().getAttribute(Constants.CACHE_USER_INFO_KEY)!=null){
            return CommonResult.success(request.getSession().getAttribute(Constants.CACHE_USER_INFO_KEY));
        }
        String loginType = request.getParameter("login_type");
        String phone = request.getParameter("phone");
        String code = request.getParameter("code");
        String userName = request.getParameter("username");
        String pwd = request.getParameter("password");
        String clientId = request.getParameter("client_id");
        CommonResult<String>result;
        if("phoneForm".equals(loginType)){
            result = oAuthService.findUserInfoByPhone(phone,code,clientId);
        }else {
            result = oAuthService.findUserInfo(userName,pwd,clientId);
        }
        if( result.isSuc()){
            request.getSession().setAttribute(Constants.CACHE_USER_INFO_KEY,result.getData());
        }
        return result;
    }

    @RequestMapping("/checkLogin")
    @ResponseBody
    public CommonResult<String> checkLogin(String param1,String param2,String clientId,String loginType){
        CommonResult<String>result;
        if("phoneForm".equals(loginType)){
            result = oAuthService.findUserInfoByPhone(param1,param2,clientId);
        }else {
            result = oAuthService.findUserInfo(param1,param2,clientId);
        }
        return result;
    }
}
