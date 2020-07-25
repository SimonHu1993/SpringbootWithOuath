package com.simonhu.oauth2.web.sourceserver.service;

import com.simonhu.oauth2.common.domain.CommonResult;
import com.simonhu.oauth2.common.shortmessage.service.ShortMessageService;
import com.simonhu.oauth2.common.util.VerificationUtil;
import com.simonhu.oauth2.web.oauth.service.OAuthService;
import com.simonhu.oauth2.web.sourceserver.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yanjun
 * @Description: TODO
 * @date 2018/1/20
 */
@Service
public class UserService {
    @Autowired
    private OAuthService oAuthService;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ShortMessageService shortMessageService;

    /**
     * 查询用户信息
     * @param accessToken
     * @return
     * {
     *     "code":"000",
     *     "msg":"",
     *     "data":{
     *         "user_info":{
     *             "phone_no":"",
     *             "user_name":"",
     *             "user_id":"",
     *             "email":"",
     *             "office_list":["1","2"]
     *         },
     *         "client_list":[
     *              {
     *                  "client_name":"",
     *                  "client_id":"",
     *                  "index_url":"",
     *                  "is_manager":""
     *              }
     *         ]
     *     }
     * }
     */
    public CommonResult finUserInfo(String accessToken) {
        String userId = oAuthService.getCacheInfoByAccessToken(accessToken,1);
        Map data = new HashMap();
        Map userInfo = oAuthService.getCacheUserInfo(userId);
        List olist = new ArrayList();
        /*for(Map officeMap :  userMapper.selUserOffice(userId)){
            olist.add(String.valueOf(officeMap.get("office_id")));
        }*/
        userInfo.put("office_list" ,olist);
        List <Map>userClientList = userMapper.findUserClientList(userId);
        data.put("user_info",userInfo);
        data.put("client_list",userClientList);
        return CommonResult.success(data);
    }

    /**
     * 查询用户权限列表
     * @param accessToken
     * @return
     */
    public CommonResult permissionsList(String accessToken) {
        String userId = oAuthService.getCacheInfoByAccessToken(accessToken,1);
        String clientId = oAuthService.getCacheInfoByAccessToken(accessToken,2);
        Map parMap = new HashMap();
        parMap.put("userId",userId);
        parMap.put("clientId",clientId);
        List<String>menuList = userMapper.permissionsList(parMap);
        return CommonResult.success(menuList);
    }

    /**
     * 发送验证码
     * @param phone
     * @return
     */
    public Map sendSms(String phone){
        Map result = new HashMap();
        if (!VerificationUtil.isMobile(phone)){
            result.put("returnCode","503");
            result.put("returnMsg","手机号格式不正确！");
            return result;
        }
        Map param = new HashMap();
        param.put("phone",phone);
        Map codeMap = new HashMap();
        List<Map> list = userMapper.selectSysAcount(param);
        if(null == list || list.isEmpty()){
            result.put("returnCode","505");
            result.put("returnMsg","该手机号暂无登录权限");
            return result;
        }
        codeMap =   shortMessageService.sendSMSPhoneCode(phone,"100");
        if(null == codeMap || codeMap.isEmpty()){
            result.put("returnCode","504");
            result.put("returnMsg","短信发送失败");
            return result;
        }
        result.put("returnCode","000");
        result.put("returnMsg","短信发送成功");
        return result;
    }



}
