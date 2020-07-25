package com.simonhu.oauth2.web.sourceserver.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @author yanjun
 * @Description: TODO
 * @date 2018/1/19
 */
@Mapper
public interface UserMapper {

    List<Map> findUserClientList(String userId);

    List<String> permissionsList(Map parMap);

    List<Map> selectSysAcount(Map map);

    List<Map> selUserOffice(String userId);
}
