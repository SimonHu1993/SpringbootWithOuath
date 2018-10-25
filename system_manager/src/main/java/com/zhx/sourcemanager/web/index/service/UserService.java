package com.zhx.sourcemanager.web.index.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhx.admin.authclient.config.OAuthClientParameters;
import com.zhx.sourcemanager.common.config.SiteConfig;
import com.zhx.admin.authclient.domain.CommonResult;
import com.zhx.sourcemanager.common.util.Sha1Util;
import com.zhx.sourcemanager.web.index.mapper.UserMapper;
import org.apache.commons.lang3.StringUtils;
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
public class UserService {

    @Autowired
    private OAuthClientParameters oAuthClientParameters;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private SiteConfig siteConfig;

    public Map listUser(int page, int pageSize, String userStatus,String phoneNo,
                        String userName,String clientIdsStr) {
        PageHelper.startPage(page, pageSize);
        Map parMap = new HashMap();
        parMap.put("userStatus",userStatus);
        parMap.put("phoneNo",phoneNo);
        parMap.put("userName",userName);
        parMap.put("clientIdsStr",clientIdsStr);
        //超级管理员需要排除在外
        if(clientIdsStr.indexOf(oAuthClientParameters.getClient_id())==-1){
            parMap.put("excludeManager",oAuthClientParameters.getClient_id());
        }
        List list = userMapper.listUser(parMap);
        PageInfo<List<Map>> pageInfo = new PageInfo(list);
        Map result = new HashMap();
        result.put("rows", pageInfo.getList());
        result.put("totalCount", pageInfo.getTotal());
        return result;
    }

    public CommonResult delUser(String userId) {
        int delNumber = userMapper.delUser(Integer.valueOf(userId));
        if(delNumber==1){
            return CommonResult.success();
        }else{
            return CommonResult.result("001","删除失败");
        }
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
    public CommonResult addUser(String userId,boolean isEdit,String userName, String loginPwd, String nickName
            , String phoneNo, String email, String clientIds, String isManagers, String roleIds,String officeIds
            , String createId) {
        //密码加密
        loginPwd = Sha1Util.encodeSha256(loginPwd,siteConfig.getPwdSalt());
        Map userMap = new HashMap();
        userMap.put("userName",userName);
        userMap.put("loginPwd",loginPwd);
        userMap.put("nickName",nickName);
        userMap.put("phoneNo",phoneNo);
        userMap.put("email",email);
        userMap.put("userId",isEdit?userId:"");
        userMap.put("createUser",createId);

        //编辑
        if(isEdit){
            Map userInfo = userMapper.selUser(userMap);
            String userPhone = String.valueOf(userInfo.get("phone_no"));
            if(!phoneNo.equals(userPhone)){
                //验证手机号是否已经存在
                int checkPhone = userMapper.checkPhone(userMap);
                if(checkPhone>0){
                    return CommonResult.result("004","手机号已存在");
                }
            }
            //修改信息
            int updateNumber = userMapper.updateUser(userMap);
            if(updateNumber!=1){
                return CommonResult.result("003","编辑失败");
            }
            //清除已经关联客户端
            userMapper.delUserClient(userMap);
            //清除已经关联角色
            userMapper.delUserRole(userMap);
        }
        //添加
        else{
            //验证手机号是否已经存在
            int checkPhone = userMapper.checkPhone(userMap);
            if(checkPhone>0){
                return CommonResult.result("004","手机号已存在");
            }
            //验证用户名是否已经存在
            int checkUser = userMapper.checkUser(userMap);
            if(checkUser>0){
                return CommonResult.result("001","登录名已存在");
            }
            //插入用户表
            int addNumber = userMapper.addUser(userMap);
            if(addNumber<1){
                return CommonResult.result("002","添加用户失败");
            }
        }
        boolean linkedManager = false;
        //插入用户与客户端关联表；
        String clientIdList [] = clientIds.split(",");
        String isManager[] = isManagers.split(",");
        String roleIdList[] = roleIds.split(",");
        for(int i=0;i<clientIdList.length;i++){
            String clientId =  clientIdList[i];
            if(clientId.equals(oAuthClientParameters.getClient_id())){
                linkedManager = true;
            }
            userMap.put("clientId",clientId);
            userMap.put("isManager",isManager[i]);
            if(clientId.trim().length()>0){
                userMapper.addUserClient(userMap);
            }
            if(i<roleIdList.length){
                String roleIdsArray[] = roleIdList[i].split("#");
                for(String roleId : roleIdsArray){
                    //插入用户与角色关联表；
                    if(StringUtils.isNotEmpty(roleId)){
                        userMap.put("roleId",roleId);
                        userMapper.addUserRole(userMap);
                    }
                }
            }
        }
        //用户分公司相关信息
        userMapper.delUserOffice(userMap);
        String officeList[] = officeIds.split(",");
        for(String off : officeList){
            if(off.trim().length() > 0) {
                userMap.put("officeId", off);
                userMapper.addUserOffice(userMap);
            }
        }

        //添加的用户没有关联管理客户端，添加关联，但不添加默认角色，（只为了登录时候验证能通过）
        if(!linkedManager){
            userMap.put("clientId",oAuthClientParameters.getClient_id());
            userMap.put("isManager","0");
            userMapper.addUserClient(userMap);
        }
        return CommonResult.success();
    }

    public List<Map>selectAllClient() {
        return userMapper.selectAllClient();
    }

    public CommonResult findUserClintRole(String userId) {
        Map parMap = new HashMap();
        parMap.put("userId",userId);
        //查询用户关联的客户端
        List<Map>clientList = userMapper.listUserClient(parMap);
        for(Map client:clientList){
            parMap.put("clientId",client.get("clientId"));
            String isManager = String.valueOf(client.get("isManager"));
            if("1".equals(isManager)){
                client.put("isManager",true);
            }else{
                client.put("isManager",false);
            }
            List<Map> userClientRole = userMapper.listUserClientRole(parMap);

            client.put("roleList",userClientRole);
        }
        List<Map> selUserOffice = userMapper.selUserOffice(parMap);
        List olist = new ArrayList();
        for(Map officeMap : selUserOffice){
            olist.add(String.valueOf(officeMap.get("id")));
        }
        Map has = new HashMap();
        has.put("clientList",clientList);
        has.put("officesList",olist);

        return CommonResult.success(has);
    }
    public CommonResult updatePassWord(String userId,String oldPassword,String newPassword) {
        Map parMap = new HashMap();
        parMap.put("userId",userId);
        //密码加密
        String oldPasswordSecurity = Sha1Util.encodeSha256(oldPassword,siteConfig.getPwdSalt());
        String newPasswordSecurity = Sha1Util.encodeSha256(newPassword,siteConfig.getPwdSalt());
        parMap.put("newPassword",newPasswordSecurity);
        //查询当前用户
        Map<String,String> userInfo = userMapper.selUser(parMap);
        if(userInfo==null || userInfo.size()<1){
            return CommonResult.result("001","查询用户有误");
        }
        String loginPwd = userInfo.get("login_pwd");
        if(!oldPasswordSecurity.equals(loginPwd)){
            return CommonResult.result("002","原密码输入有误");
        }
        int count = userMapper.updateUserPassWord(parMap);
        if(count!=1){
            return CommonResult.result("003","密码修改有误，请重试");
        }
        return CommonResult.success();
    }

    public CommonResult selOffices(){
        List list = userMapper.selOffices();
        if(list != null && list.size()>0) {
            return CommonResult.success(userMapper.selOffices());
        }else{
            return CommonResult.result("001", "查询公司列表错误");
        }

    }


}
