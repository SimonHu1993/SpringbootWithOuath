package com.simonhu.sourcemanager;

import com.simonhu.admin.authclient.interceptor.LogoutInterceptor;
import com.simonhu.admin.authclient.interceptor.OauthInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import com.simonhu.sourcemanager.web.interceptor.LogInterceptor;

/**
 * Created by yj on 2017-07-04.
 * 备注：
 *      ComponentScan默认只扫描@SpringBootApplication所注解类所在的包。
 *      oauth拦截器配置相关的都放在oauth_client中,所以这里手动指定两个包。
 *
 */
@SpringBootApplication
@ComponentScan(basePackages={"com.simonhu.sourcemanager","com.simonhu.admin.authclient"})
public class SysManagerApplication extends WebMvcConfigurerAdapter {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(SysManagerApplication.class, args);
    }

    @Autowired
    private OauthInterceptor oauthInterceptor;
    @Autowired
    private LogoutInterceptor logoutInterceptor;
    @Autowired
    private LogInterceptor logInterceptor;

    /**
     * 添加拦截器
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        super.addInterceptors(registry);
        registry.addInterceptor(logInterceptor).addPathPatterns("/**");

        registry.addInterceptor(oauthInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/logout");
        //退出
        registry.addInterceptor(logoutInterceptor)
                .addPathPatterns("/logout");

    }

}
