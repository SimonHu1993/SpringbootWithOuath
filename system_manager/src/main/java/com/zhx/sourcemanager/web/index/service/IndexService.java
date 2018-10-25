package com.zhx.sourcemanager.web.index.service;

import com.zhx.admin.authclient.config.OAuthClientParameters;
import com.zhx.admin.authclient.domain.CommonResult;
import com.zhx.admin.authclient.service.OauthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yj on 2018-01-21.
 */
@Service
public class IndexService {

    @Autowired
    private  UserService userService;
    @Autowired
    private OAuthClientParameters oAuthClientParameters;

    public List<Map>findShowClientList(List<Map> clientList) {
        List managerClientList = new ArrayList<>();
        boolean isSystemManager = false;
        for(Map client:clientList){
            if("1".equals(String.valueOf(client.get("is_manager")))){
                managerClientList.add(client);
                if(oAuthClientParameters.getClient_id().equals(String.valueOf(client.get("client_id")))){
                    isSystemManager = true;//为管理系统管理员
                }
            }
        }
        //超级管理员显示所有的客户端
        if(isSystemManager){
            managerClientList = userService.selectAllClient();
        }
        return managerClientList;
    }
}
