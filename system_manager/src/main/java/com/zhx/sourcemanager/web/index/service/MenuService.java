package com.zhx.sourcemanager.web.index.service;

import com.zhx.admin.authclient.domain.CommonResult;
import com.zhx.sourcemanager.web.index.mapper.MenuMapper;
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
public class MenuService {

    @Autowired
    private MenuMapper menuMapper;


    /**
     * 加载菜单节点
     * @param pid 父节点，  第一级根节点为0
     * @param clientId  客户端id
     * @return
     */
    public CommonResult loadSonMenuList(String pid, String clientId) {
        Map map = new HashMap();
        map.put("pid",pid);
        map.put("clientId",clientId);
        List<Map> menuList = menuMapper.loadSonMenuList(map);
        menuList.forEach(
                 menuItem ->{
                     menuItem.put("sonMenuList", new ArrayList());
                     Object hasSon = menuItem.get("hasSon");
                     if("1".equals(String.valueOf(hasSon))){
                         menuItem.put("hasSon",true);
                     }else{
                         menuItem.put("hasSon",false);
                     }
                }
        );
        return CommonResult.success(menuList);
    }

    public CommonResult add(String menuUrl, String menuName, int orderBy, int menuType, int menuPid, String clientId,String menuId) {
        Map map = new HashMap();
        map.put("menuUrl",menuUrl);
        map.put("menuName",menuName);
        map.put("orderBy",orderBy);
        map.put("menuType",menuType);
        map.put("menuPid",menuPid);
        map.put("clientId",clientId);
        if(StringUtils.isEmpty(menuId)){
            map.put("menuId","");
            int updateNum = menuMapper.addMenu(map);
            if(menuPid!=0){
                menuMapper.updateMenuHasSon(menuPid);
            }
        }else{
            map.put("menuId",menuId);
            menuMapper.updateMenu(map);
        }


        return CommonResult.success(map.get("menuId"));
    }

    public CommonResult del(int menuId) {
        menuMapper.delMenu(menuId);
        delSonMenu(menuId);
        return CommonResult.success();
    }

    private void delSonMenu(int menuId){
        List<Integer> sonMent = menuMapper.findSonMenu(menuId);
        for(Integer sonMenuId : sonMent){
            menuMapper.delMenu(sonMenuId);
            delSonMenu(sonMenuId);
        }
    }

    public CommonResult<List> allMenu(String clientId) {
        List<Map>menuList = menuMapper.listMenuByClientId(clientId);
        menuList.forEach(
                menuItem ->{
                    Object hasSon = menuItem.get("hasSon");
                    if("1".equals(String.valueOf(hasSon))){
                        menuItem.put("hasSon",true);
                    }else{
                        menuItem.put("hasSon",false);
                    }
                }
        );
        return CommonResult.success(formatMenuList(menuList,"0"));
    }

    private List<Map> formatMenuList(List<Map>menuList,String pid){
        List<Map>sonMenuList = new ArrayList();
        for(Map map:menuList){
            if(pid.equals(String.valueOf(map.get("pid")))){
                sonMenuList.add(map);
            }
        }
        for(Map map : sonMenuList){
            List<Map> tmpSonMenuList = formatMenuList(menuList,String.valueOf(map.get("menuId")));
            map.put("sonMenuList",tmpSonMenuList);
        }
        return sonMenuList;
    }

    /**
     * 查询用户关联客户端已经分配的菜单
     * @param userId
     * @param clientId
     * @return
     */
    public List<Map> listUserClientMenu(String userId, String clientId) {
        Map parMap = new HashMap();
        parMap.put("userId",userId);
        parMap.put("clientId",clientId);
        List<Map>menuList = menuMapper.listUserClientMenu(parMap);
        menuList.forEach(
                menuItem ->{
                    Object hasSon = menuItem.get("hasSon");
                    if("1".equals(String.valueOf(hasSon))){
                        menuItem.put("hasSon",true);
                    }else{
                        menuItem.put("hasSon",false);
                    }
                }
        );
        return formatMenuList(menuList,"0");
    }

}
