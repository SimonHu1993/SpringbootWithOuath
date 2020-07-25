package com.simonhu.sourcemanager.web.index.controller;

import com.simonhu.admin.authclient.domain.CommonResult;
import com.simonhu.sourcemanager.web.index.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;

/**
 * @author yanjun
 * @Description: TODO
 * @date 2018/1/19
 */
@Controller
@RequestMapping("/menu")
public class MenuController {

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
//        mav.setViewName("menu/index");
//        return mav;
//    }

    @RequestMapping("/loadSonMenuList")
    public @ResponseBody CommonResult loadSonMenuList(HttpServletRequest request, String pid, String clientId){
        CommonResult result = menuService.loadSonMenuList(pid,clientId);
        return result;
    }

    @RequestMapping("/add")
    public @ResponseBody CommonResult add(HttpServletRequest request,
        String menuUrl, String menuName,int orderBy,int menuType, int menuPid,String clientId ,String menuId
    ){
        CommonResult result = menuService.add(menuUrl, menuName,orderBy, menuType, menuPid,clientId ,menuId);
        return result;
    }

    @RequestMapping("/del")
    public @ResponseBody CommonResult del(HttpServletRequest request,
                                          int menuId
    ){
        CommonResult result =  menuService.del( menuId);
        return result;
    }


}
