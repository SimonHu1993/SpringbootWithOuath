package com.simonhu.sourcemanager.web.index.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @author yanjun
 * @Description: TODO
 * @date 2018/1/19
 */
@Mapper
public interface RoleMapper {

    List listRole(String clientId);

    int delRole(int roleId);

    int addRole(Map parMap);

    int addRoleMenu(Map parMap);

    List<Integer> findRoleMenuByRoleId(String roleId);

    int updateRole(Map parMap);

    void delRoleMenu(int roleId);

}
