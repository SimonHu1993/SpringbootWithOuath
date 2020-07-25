package com.simonhu.oauth2.web.oauth.service;

import com.simonhu.oauth2.common.config.SiteConfig;
import com.simonhu.oauth2.common.domain.CommonResult;
import com.simonhu.oauth2.common.shortmessage.service.ShortMessageService;
import com.simonhu.oauth2.common.util.JsonUtils;
import com.simonhu.oauth2.common.util.Sha1Util;
import com.simonhu.oauth2.common.util.VerificationUtil;
import com.simonhu.oauth2.web.oauth.mapper.OAuthMapper;
import org.ehcache.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yj on 2017-07-04.
 */
@Service
public class OAuthService {

    public static final Logger log = LoggerFactory.getLogger(OAuthService.class);

    @Autowired
    private Cache authCodeCache;
    @Autowired
    private Cache accessTokenCache;
    @Autowired
    private Cache userInfoCache;
    @Autowired
    private OAuthMapper oAuthMapper;
    @Autowired
    private SiteConfig siteConfig;
    @Autowired
    private ShortMessageService shortMessageService;

    /**
     * 验证clientId  clientKey
     * @param clientId
     * @param clientKey
     * @return
     */
    public boolean checkClientId(String clientId,String clientKey) {
        Map clientInfo = this.findClientInfo(clientId,clientKey);
        if(clientInfo==null||clientInfo.isEmpty()){
            return false;
        }
        return true;
    }

    /**
     * 查询客户端信息
     * @param clientId
     * @param clientKey
     * @return
     */
    public Map findClientInfo(String clientId,String clientKey){
        Map parMap = new HashMap();
        parMap.put("clientId",clientId);
        parMap.put("clientKey",clientKey);
        Map clientInfo = oAuthMapper.findClientInfo(parMap);
        return clientInfo;
    }

    /**
     * 登录接口查询用户信息
     * @param userName
     * @param pwd
     * @param clientId
     * @return
     */
    public CommonResult<String>findUserInfo(String userName, String pwd, String clientId) {
        //密码加密
        pwd = Sha1Util.encodeSha256(pwd,siteConfig.getPwdSalt());
        Map parMap = new HashMap();
        parMap.put("userName",userName);
        parMap.put("pwd",pwd);
        parMap.put("clientId",clientId);
        Map userInfoMap = oAuthMapper.findUserInfo(parMap);
        if(userInfoMap==null||userInfoMap.isEmpty()){
            return CommonResult.result("001","用户名或密码错误");
        }else{
            String userId = String.valueOf(userInfoMap.get("user_id"));
            //缓存用户基本信息
            cacheUserInfo(userId,userInfoMap);
            return  CommonResult.success(userId);
        }
    }
    /**
     * 登录接口查询用户信息(手机号登录)
     * @param phone
     * @param code
     * @param clientId
     * @return
     */
    public CommonResult<String>findUserInfoByPhone(String phone, String code, String clientId) {
        Map result = new HashMap();
        if (!VerificationUtil.isMobile(phone)){
            return CommonResult.result("999","手机号有误");
        }
        boolean verify = checkCode(phone, code);
        if(!verify){
            return CommonResult.result("998","验证码有误");
        }
        Map parMap = new HashMap();
        parMap.put("phone",phone);
        parMap.put("clientId",clientId);
        Map userInfoMap = oAuthMapper.findUserInfoByPhone(parMap);
        if(userInfoMap==null||userInfoMap.isEmpty()){
            return CommonResult.result("001","用户不存在");
        }else{
            String userId = String.valueOf(userInfoMap.get("user_id"));
            //缓存用户基本信息
            cacheUserInfo(userId,userInfoMap);
            return  CommonResult.success(userId);
        }
    }
    public boolean checkCode(String name,String code){
        int i =shortMessageService.updatePhoneMessage(name,code);
        if(i==1){
            return true;
        }else{
            return false;
        }
    }

    /**
     * 登录后缓存用户信息
     * @param key  组成：  userId
     * @param userInfo
     */
    public void cacheUserInfo(String key, Map userInfo) {
        String userInfoStr = JsonUtils.toJsonNf(userInfo);
        log.info("登录完成后缓存用户信息：{}",userInfoStr);
        userInfoCache.remove(key);
        userInfoCache.put(key, userInfoStr );
    }

    /**
     * 获取用户信息
     * @param key  组成：  userId
     */
    public Map getCacheUserInfo(String key) {
        Object str = userInfoCache.get(key);
        Map userInfo = JsonUtils.toObject(String.valueOf(str),HashMap.class);
        return userInfo;
    }

    /**
     * 缓存auth_code
     * @param authorizationCode
     * @param userId
     */
    public void addAuthCode(String authorizationCode, String userId) {
        authCodeCache.put(authorizationCode,userId);
    }

    /**
     * 验证 authCode
     * @param authCode
     * @return
     */
    public Object checkAuthCode(String authCode) {
        Object obj = authCodeCache.get(authCode);
        return obj;
    }

    /**
     * 缓存accesstoken与userId 和 clientId的对应关系
     * @param accessToken
     * @param userId
     * @param clientId
     */
    public void addAccessToken(String accessToken, String userId,String clientId) {
        accessTokenCache.put(accessToken+"_USERID",userId);
        accessTokenCache.put(accessToken+"_CLIENTID",clientId);
    }

    /**
     * 缓存accesstoken与userId 和 clientId的对应关系
     * @param accessToken
     * @param type      1查询userId  2查询clientId
     */
    public String getCacheInfoByAccessToken(String accessToken,int type) {
        Object obj = null;
        if(type==1){
            obj = accessTokenCache.get(accessToken+"_USERID");
        }else if(type==2){
            obj = accessTokenCache.get(accessToken+"_CLIENTID");
        }
        if(StringUtils.isEmpty(obj)){
            return null;
        }else{
            return String.valueOf(obj);
        }

    }

    /**
     * 查询当前用户下所有系统logout地址(除去系统管理系统)
     * @param userId
     * @param clientId
     * @return
     */
    public List findClientLogoutList(String userId,String clientId){
        Map parMap = new HashMap();
        parMap.put("userId",userId);
        parMap.put("clientId",clientId);
        return oAuthMapper.findClientLogoutList(parMap);
    }



}
