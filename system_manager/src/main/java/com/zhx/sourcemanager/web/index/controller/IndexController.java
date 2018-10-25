package com.zhx.sourcemanager.web.index.controller;

import com.zhx.admin.authclient.config.OAuthClientParameters;
import com.zhx.sourcemanager.common.config.Constants;
import com.zhx.admin.authclient.domain.CommonResult;
import com.zhx.sourcemanager.web.index.service.IndexService;
import com.zhx.sourcemanager.web.index.service.MenuService;
import com.zhx.sourcemanager.web.index.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author yanjun
 * @Description: TODO
 * @date 2018/1/19
 */
@Controller
public class IndexController {
    @Autowired
    private IndexService indexService;
    @Autowired
    private MenuService menuService;
    @Autowired
    private OAuthClientParameters oAuthClientParameters;

    @RequestMapping("/index")
    public ModelAndView index(HttpServletRequest request){
        ModelAndView mav = new ModelAndView();
        CommonResult<Map>result = (CommonResult<Map>) request.getSession().getAttribute(Constants.CACHE_USER_INFO);
        List<Map>clientList = (List) result.getData().get("client_list");

        //判断登录用户是否为管理员，如果不是管理员则在管理界面中选择客户端中去除管理客户端。
        boolean isManager = false;
        int managerClientIndex = -1;//管理客户端下标
        for(int i=0;i<clientList.size();i++){
            String sessionClientId = String.valueOf(clientList.get(i).get("client_id"));
            String sessionIsManager = String.valueOf(clientList.get(i).get("is_manager"));
            if("1".equals(sessionIsManager)){
                isManager=true;
            }
            if(sessionClientId.equals(oAuthClientParameters.getClient_id())){
                managerClientIndex=i;
            }
        }
        if(!isManager&&managerClientIndex!=-1){
            clientList.remove(managerClientIndex);
        }
        //显示默认客户端默认菜单
        String clientId = String.valueOf(clientList.get(0).get("client_id"));
        String userId = String.valueOf(((Map)result.getData().get("user_info")).get("user_id"));
        List <Map>userClientMenu = menuService.listUserClientMenu(userId,clientId);
        mav.addObject("data",result.getData());
        mav.addObject("defaultClient",clientId);
        mav.addObject("defaultMenuList",userClientMenu);
        mav.setViewName("index");
        return mav;
    }

    @RequestMapping("/userClientMenu")
    public @ResponseBody CommonResult userClientMenu(HttpServletRequest request,String clientId){
        ModelAndView mav = new ModelAndView();
        CommonResult<Map>result = (CommonResult<Map>) request.getSession().getAttribute(Constants.CACHE_USER_INFO);
        mav.addObject("data",result.getData());
        String userId = String.valueOf(((Map)result.getData().get("user_info")).get("user_id"));
        List <Map>userClientMenu = menuService.listUserClientMenu(userId,clientId);
        return CommonResult.success(userClientMenu);
    }

    @RequestMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response){
        request.getSession().invalidate();
    }

    /**
     * 页面导航
     * @param request
     * @param pageName  值：  user   menu  role  client
     * @return
     */
    @RequestMapping("/page/{pageName}")
    public ModelAndView index(HttpServletRequest request,
                              @PathVariable(name="pageName") String pageName){
        ModelAndView mav = new ModelAndView();
        CommonResult<Map>result = (CommonResult<Map>) request.getSession().getAttribute(Constants.CACHE_USER_INFO);
        List<Map>clientList = (List) result.getData().get("client_list");
        List managerClientList = indexService.findShowClientList(clientList);
        //显示管理客户端列表
        mav.addObject("managerClientList",managerClientList);
        mav.setViewName(pageName+"/"+pageName);
        return mav;
    }


}
