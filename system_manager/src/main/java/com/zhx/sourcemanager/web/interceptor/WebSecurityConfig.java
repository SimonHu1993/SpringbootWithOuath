package com.zhx.sourcemanager.web.interceptor;

/**
 * Created by huangds on 2017/10/24.
 */

import com.zhx.admin.authclient.config.Constants;
import com.zhx.admin.authclient.domain.CommonResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;

/**
 * 登录配置
 */
@Configuration
public class WebSecurityConfig extends WebMvcConfigurerAdapter{
    public final static String SESSION_KEY="username";
    public final static String USER_ID="user_id";
    public final static String OFFICE_ID="office_id";

    public final static String USER_INFO="user_info";
    public final static String PHONE_NO="phone_no";
    @Value("${server.contextPath}")
    private  String contextPath;
    @Bean
    public SecurityInterceptor getSecurityInterceptor(){
        return new SecurityInterceptor();
    }
   @Override
    public void  addInterceptors(InterceptorRegistry registry){
        InterceptorRegistration addInterceptor = registry.addInterceptor(getSecurityInterceptor());
        addInterceptor.excludePathPatterns("/error");
        addInterceptor.excludePathPatterns("/login**");
        addInterceptor.excludePathPatterns("/sendSms");
        addInterceptor.addPathPatterns("/**");

    }

    private class SecurityInterceptor extends HandlerInterceptorAdapter{
        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response,Object handler) throws IOException {
            HttpSession session = request.getSession();
//            判断是否已有该用户登录的session
            if(session.getAttribute(SESSION_KEY) != null){
                return true;
            }
            CommonResult result = (CommonResult) request.getSession().getAttribute(Constants.CACHE_USER_INFO);
            if(result.isSuc()){
                Map map = (Map)result.getData();
                Map<String,String> userInfo = (Map)map.get(USER_INFO);
                session.setAttribute(SESSION_KEY,userInfo.get(PHONE_NO));
                session.setAttribute(OFFICE_ID,userInfo.get(OFFICE_ID));
                session.setAttribute(USER_ID,userInfo.get(USER_ID));
                return true;
            }else {
                return false;
            }
//            跳转到登录页
//            String url = contextPath+"/login";
//            response.sendRedirect(url);
//            return true;
        }
    }
}
