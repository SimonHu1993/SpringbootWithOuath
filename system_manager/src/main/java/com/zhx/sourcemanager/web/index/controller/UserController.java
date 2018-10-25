package com.zhx.sourcemanager.web.index.controller;

import com.zhx.admin.authclient.domain.CommonResult;
import com.zhx.sourcemanager.common.config.Constants;
import com.zhx.sourcemanager.web.index.service.IndexService;
import com.zhx.sourcemanager.web.index.service.UserService;
import com.zhx.sourcemanager.web.interceptor.WebSecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author yanjun
 * @Description: TODO
 * @date 2018/1/19
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private IndexService indexService;

    /**
     * 查询用户列表
     * @param page
     * @param pageSize
     * @param userStatus    1有效  0无效
     * @param phoneNo
     * @param userName
     * @return
     */
    @RequestMapping("listUser")
    public  @ResponseBody Map listUser(HttpServletRequest request, int page, int pageSize, String userStatus, String phoneNo, String userName){
        CommonResult<Map>result = (CommonResult<Map>) request.getSession().getAttribute(Constants.CACHE_USER_INFO);
        List<Map> clientList = (List) result.getData().get("client_list");

        //获取用户用户所管理的客户端id，防止查询的用户超出范围
        List<Map>userClientList = indexService.findShowClientList(clientList);
        String clientIdsStr="";
        for(Map client:userClientList){
            String clientId=String.valueOf(client.get("client_id"));
            clientIdsStr+= clientId+",";
        }
        clientIdsStr = clientIdsStr.substring(0,clientIdsStr.length()-1);
        return userService.listUser(page,pageSize,userStatus,phoneNo,userName,clientIdsStr);
    }

    @RequestMapping("delUser")
    public @ResponseBody CommonResult delUser(String userId){
        return userService.delUser(userId);
    }

    /**
     * 添加用户
     * @param userId
     * @param isEdit   true 修改，false,添加。
     * @param userName  登录名
     * @param loginPwd  登录密码
     * @param nickName  姓名
     * @param phoneNo   手机号
     * @param email     email
     * @param clientIds 关联客户端       多个客户端id以逗号隔开   "123123,123123,4324324,"
     * @param isManagers    是否是管理员  是否是管理员, 1 是， 0否， 多个值以逗号隔开，  顺序与clientIds 一致
     * @param roleIds   客户端关联角色列表  多个值以逗号隔开，  顺序与clientIds 一致
     *                  （一个客户端可以对应多个角色，同一个客户端多个角色之间以# 隔开）
     *                  如：  '120123#112#,3132#,'
     * @return
     */
    @RequestMapping("addUser")
    public @ResponseBody CommonResult addUser(HttpServletRequest request, String userId,boolean isEdit,String userName,String loginPwd,String nickName,String phoneNo,String email,String clientIds,String isManagers,String roleIds, String officeIds){
        String createId = String.valueOf(request.getSession().getAttribute(WebSecurityConfig.USER_ID));

        return userService.addUser( userId, isEdit, userName, loginPwd, nickName, phoneNo, email, clientIds, isManagers, roleIds, officeIds, createId);
    }

    @RequestMapping("findUserClintRole")
    public @ResponseBody CommonResult findUserClintRole(String userId){
        return userService.findUserClintRole(userId);
    }
    @RequestMapping("updatePassWord")
    public @ResponseBody CommonResult updatePassWord(String userId,String oldPassword,String newPassword){
        return userService.updatePassWord(userId,oldPassword,newPassword);
    }
    @RequestMapping("selOffices")
    public @ResponseBody CommonResult selOffices(){
        return userService.selOffices();
    }
}
