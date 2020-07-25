package com.simonhu.oauth2.web.sourceserver.controller;

import com.simonhu.oauth2.common.domain.CommonResult;
import com.simonhu.oauth2.web.sourceserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author yanjun
 * @Description: 用户信息查询
 * @date 2018/1/20
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 查询用户基本信息
     * @param accessToken
     * @return
     */
    @RequestMapping("/info")
    public CommonResult userInfo(@RequestParam(name="accessToken") String accessToken){
        CommonResult result = userService.finUserInfo(accessToken);
        return result;
    }

    /**
     * 查询用户权限列表
     * @param accessToken
     * @return
     */
    @RequestMapping("/permissions")
    public CommonResult permissionsList(@RequestParam(name="accessToken") String accessToken){
        CommonResult result = userService.permissionsList(accessToken);
        return result;
    }

    /**
     * 验证用户是否有当前URL的访问权限
     * @param accessToken
     * @param operationUrl  操作 url
     * @return
     */
    @RequestMapping("/permissions/validate")
    public CommonResult permissions(@RequestParam(name="accessToken") String accessToken,
                                        @RequestParam(name="operationUrl") String operationUrl
                                        ){
        CommonResult result = userService.permissionsList(accessToken);
        return result;
    }

    /**
     * 发送验证码
     * @param phone
     * @return
     */
    @RequestMapping("/sendSms")
    @ResponseBody
    public Map sendSms(String phone) {
        Map result = userService.sendSms(phone);
        return result;
    }


}
