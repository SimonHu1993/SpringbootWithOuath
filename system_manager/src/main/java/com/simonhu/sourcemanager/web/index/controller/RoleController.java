package com.simonhu.sourcemanager.web.index.controller;

import com.simonhu.admin.authclient.domain.CommonResult;
import com.simonhu.sourcemanager.web.index.service.MenuService;
import com.simonhu.sourcemanager.web.index.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author yanjun
 * @Description: TODO
 * @date 2018/1/19
 */
@Controller
@RequestMapping("/role")
public class RoleController {

    @Autowired
    private RoleService roleService;
    @Autowired
    private MenuService menuService;

//    @RequestMapping("")
//    public ModelAndView index(HttpServletRequest request){
//        ModelAndView mav = new ModelAndView();
//        CommonResult<Map>result = (CommonResult<Map>) request.getSession().getAttribute(Constants.CACHE_USER_INFO);
//        List<Map>clientList = (List) result.getData().get("client_list");
//        List managerClientList = new ArrayList<>();
//        for(Map client:clientList){
//            if("1".equals(String.valueOf(client.get("is_manager")))){
//                managerClientList.add(client);
//            }
//        }
//        //显示管理客户端列表
//        mav.addObject("managerClientList",managerClientList);
//        mav.setViewName("role/index");
//        return mav;
//    }

    @RequestMapping("list")
    public  @ResponseBody Map listRole(int page, int pageSize,String clientId){
        return roleService.listRole(page,pageSize,clientId);
    }

    @RequestMapping("delRole")
    public  @ResponseBody CommonResult delRole(int roleId){
        return roleService.delRole(roleId);
    }

    /**
     * 添加角色
     * @param clientId
     * @param roleName
     * @param roleMenuIds   角色关联的菜单id， 多个id之间以逗号分隔。 如： 2,4,5,6,3,
     * @param submitType    0添加，1 修改
     * @return
     */
    @RequestMapping("addRole")
    public  @ResponseBody CommonResult addRole(String clientId,String roleName,String roleMenuIds,String submitType,String roleId){
        if("0".equals(submitType)){
            return  roleService.addRole(clientId,roleName,roleMenuIds);
        }else{
            return  roleService.editRole(clientId,roleName,roleMenuIds,roleId);
        }

    }

    @RequestMapping("findAllMenu")
    public  @ResponseBody CommonResult findAllMenu(String clientId){
        return menuService.allMenu(clientId);
    }

    @RequestMapping("findRoleMenu")
    public  @ResponseBody CommonResult findRoleMenu(String roleId){
        return roleService.findRoleMenuByRoleId(roleId);
    }

    @RequestMapping("loadClientRole")
    public  @ResponseBody CommonResult loadClientRole(String clientId){
        return roleService.loadClientRole(clientId);
    }









}
