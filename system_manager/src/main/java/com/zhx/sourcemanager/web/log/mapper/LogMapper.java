package com.zhx.sourcemanager.web.log.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.Map;


/**
 * @author liyongxing
 * @Description: TODO
 * @date 2018/4/23
 */
@Mapper
@Repository
public interface LogMapper {
    /**
     * 保存客户端请求日志
     * @param map
     * @return
     */
    int insertOauthClientLog(Map map);
}
