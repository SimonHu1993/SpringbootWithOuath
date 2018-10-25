package com.zhx.oauth2.web.oauth.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @author yanjun
 * @Description: TODO
 * @date 2018/1/19
 */
@Mapper
public interface OAuthMapper {

    Map findClientInfo(Map parMap);

    Map findUserInfo(Map parMap);


    /**
     * 查询当前用户下所有系统logout地址(除去系统管理系统)
     * @param parMap
     * @return
     */
    List findClientLogoutList(Map parMap);

    Map findUserInfoByPhone(Map parMap);
}
