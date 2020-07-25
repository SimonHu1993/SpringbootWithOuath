package com.simonhu.admin.authclient.service;

import com.simonhu.admin.authclient.config.OAuthClientParameters;
import com.simonhu.admin.authclient.domain.CommonResult;
import com.simonhu.admin.authclient.util.HttpUtils;
import com.simonhu.admin.authclient.util.JsonUtils;
import com.simonhu.admin.authclient.util.SignUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yj on 2018-01-21.
 */
@Service
public class OauthService {

    @Autowired
    private OAuthClientParameters oAuthClientParameters;

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
     *             "email":""
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
    public CommonResult<Map> findUserInfo(String accessToken) {
        Map parMap = new HashMap<>();
        parMap.put("accessToken",accessToken);
        String sign = SignUtil.buildSign(parMap, oAuthClientParameters.getOauth_client_sign_key());
        parMap.put("sign",sign);
        String str = HttpUtils.doPost(oAuthClientParameters.getOauth_user_info_url(), parMap, null);
        CommonResult<Map>result = JsonUtils.toObject(str, CommonResult.class);
        if(!result.isSuc()){
            return result;
        }
        return result;
    }

    /**
     * 查询用户当前客户端的菜单列表
     * @param accessToken
     * @return
     * {
     *     "code":"000",
     *     "msg":"",
     *     "data":[
     *              "url1",
     *              ""url2,
     *              "url3"
     *             ]
     * }
     */
    public CommonResult findUserMenuList(String accessToken){
        Map parMap = new HashMap<>();
        parMap.put("accessToken",accessToken);
        String sign = SignUtil.buildSign(parMap, oAuthClientParameters.getOauth_client_sign_key());
        parMap.put("sign",sign);
        String str = HttpUtils.doPost(oAuthClientParameters.getOauth_user_menu_list_url(), parMap, null);
        CommonResult result = JsonUtils.toObject(str, CommonResult.class);
        if(!result.isSuc()){
            return result;
        }
        return result;
    }
}
