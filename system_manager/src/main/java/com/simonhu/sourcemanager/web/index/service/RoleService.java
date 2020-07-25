package com.simonhu.sourcemanager.web.index.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.simonhu.admin.authclient.domain.CommonResult;
import com.simonhu.sourcemanager.web.index.mapper.MenuMapper;
import com.simonhu.sourcemanager.web.index.mapper.RoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yj on 2018-01-21.
 */
@Service
public class RoleService {

    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private MenuMapper menuMapper;

    public Map listRole(int page, int pageSize, String clientId) {
        PageHelper.startPage(page, pageSize);
        List list = roleMapper.listRole(clientId);
        PageInfo<List<Map>> pageInfo = new PageInfo(list);
        Map result = new HashMap();
        result.put("rows", pageInfo.getList());
        result.put("totalCount", pageInfo.getTotal());
        return result;
    }

    public CommonResult delRole(int roleId) {
        int delNumber = roleMapper.delRole(roleId);
        roleMapper.delRoleMenu(roleId);
        if(delNumber==1){
            return CommonResult.success();
        }else{
            return CommonResult.result("001","删除失败");
        }
    }

    public CommonResult addRole(String clientId, String roleName, String roleMenuIds) {
        Map parMap = new HashMap();
        parMap.put("roleName",roleName);
        parMap.put("clientId",clientId);
        parMap.put("roleId","");
        parMap.put("status",1);
        int insertNumber = roleMapper.addRole(parMap);
        if(insertNumber==1&&!"".equals(String.valueOf(parMap.get("roleId")))){
            //element ui 子节点未全部选中时，需要手动查询出子节点包含的父节点。
            List<Integer>menuList = findVaildMenuPid(roleMenuIds,clientId);
            for(Integer menuId : menuList){
                parMap.put("menuId",menuId);
                roleMapper.addRoleMenu(parMap);
            }
        }
        return CommonResult.success();
    }

    public CommonResult editRole(String clientId, String roleName, String roleMenuIds, String roleId) {
        Map parMap = new HashMap();
        parMap.put("roleName",roleName);
        parMap.put("clientId",clientId);
        parMap.put("roleId",roleId);
        parMap.put("status",1);
        int updateNumber = roleMapper.updateRole(parMap);
        if(updateNumber==1){
            roleMapper.delRoleMenu(Integer.valueOf(roleId));
            //element ui 子节点未全部选中时，需要手动查询出子节点包含的父节点。
            List<Integer>menuList = findVaildMenuPid(roleMenuIds,clientId);
            for(Integer menuId : menuList){
                parMap.put("menuId",menuId);
                roleMapper.addRoleMenu(parMap);
            }
            return CommonResult.success();
        }else{
            return CommonResult.result("001","删除编辑失败");
        }

    }

    /**
     * 查询menuid的父级id列表
     * @param menuIds           element ui tree控件  子节点未全部选中时候，父节点不会提交，这里使用程序查询。
     * @return
     */
    private List<Integer> findVaildMenuPid(String menuIds,String clientId){
        List<Integer>allMenuList = new ArrayList();
        List<Map>menuList = menuMapper.listMenuByClientId(clientId);
        String menuIdStr [] = menuIds.split(",");
        for(String menuId : menuIdStr){
            if(!StringUtils.isEmpty(menuId)){
                allMenuList.add(Integer.valueOf(menuId));
                List<Integer> pidList =  findVaildMenuPid(menuId,menuList);
                addToAllPidMenuList(allMenuList,pidList);
            }
        }
        return allMenuList;
    }

    //去重
    private void addToAllPidMenuList(List<Integer>allMenuList,List<Integer> pidList){
        for(Integer pid : pidList){
            if(!allMenuList.contains(pid)){
                allMenuList.add(pid);
            }
        }
    }
    //查询父节点id列表
    private List<Integer> findVaildMenuPid(String menuId,List<Map> menuList){
        List<Integer>pidList = new ArrayList();
        for(Map item:menuList){
            if(String.valueOf(item.get("menuId")).equals(menuId)){
                String pid = String.valueOf(item.get("pid"));
                //排除 根节点0
                if(!"0".equals(pid)){
                    pidList.add(Integer.valueOf(pid));
                }
                List pidLists = findVaildMenuPid(pid,menuList);
                pidList.addAll(pidLists);
            }
        }
        return pidList;
    }

    public CommonResult findRoleMenuByRoleId(String roleId) {
        List<Integer>roleIds = roleMapper.findRoleMenuByRoleId(roleId);
        return CommonResult.success(roleIds);
    }

    public CommonResult loadClientRole(String clientId) {
        List list = roleMapper.listRole(clientId);
        return CommonResult.success(list);
    }
}
