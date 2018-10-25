package com.zhx.oauth2;

import com.zhx.oauth2.web.interceptor.BaseInterceptor;
import com.zhx.oauth2.web.interceptor.SourceServerInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * oatuh2授权：
 * 服务情况：客户端A  客户端B   OAuthServer
 *
 * 第一次访问客户端A
 *      客户端流程：
 *      C1:客户端A携带参数oauth基本参数，请求OAuthServer获取code
 *      C2:客户端A 携带参数oauth基本参数与 code ，请求OAuthServer获取accessToken
 *          缓存accessToken到session  30分钟
 *          缓存userInfo到session     30分钟
 *      服务端流程：
 *      S1：验证auth基本参数, 判断是否登录（session中是否有userId）。
 *          已经登录则跳转直接返回coed，
 *          未登录跳转到登录页面，登录成功后返回code
 *          缓存信息code : userId     5分钟
 *          缓存userId到session       30分钟
 *          缓存userId : userInfo     60分钟
 *      S2:验证auth基本参数,验证code合法性,返回accessToken
 *          通过code 获取userId,
 *          缓存 accessToken : userId   60分钟
 *          缓存 accessToken : clientId 60分钟
 *
 *  第一次访问客户端B
 *      客户端流程：
 *      C1:客户端A携带参数oauth基本参数，请求OAuthServer获取code
 *      C2:客户端A 携带参数oauth基本参数与 code ，请求OAuthServer获取accessToken
 *          缓存accessToken到session    30分钟
 *          缓存userInfo到session       30分钟
 *      服务端流程：
 *      S1：验证auth基本参数,session中有userId
 *          返回code
 *          缓存code:userId           5分钟
 *      S2:验证auth基本参数,验证code合法性,返回accessToken
 *          通过code 获取userId,
 *          缓存 accessToken : userId    60分钟
 *          缓存 accessToken : clientId  60分钟
 *
 *  客户端访问资源服务：
 *      资源服务验证accessToken合法性，验证签名。
 *      通过accessToken获取userId,查询对应用户信息
 *
 *
 *
 *
 *
 *
 *
 * Created by yj on 2017-07-04.
 */
@SpringBootApplication
public class OAuthServerApplication extends WebMvcConfigurerAdapter {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(OAuthServerApplication.class, args);
    }

    @Autowired
    private BaseInterceptor baseInterceptor;
    @Autowired
    private SourceServerInterceptor sourceServerInterceptor;


    /**
     * 添加拦截器
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        super.addInterceptors(registry);
        registry.addInterceptor(baseInterceptor)
                .addPathPatterns("/**");

        registry.addInterceptor(sourceServerInterceptor)
                .addPathPatterns("/user/**").excludePathPatterns("/user/sendSms");


    }

}
