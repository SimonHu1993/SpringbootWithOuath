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
public interface UserMapper {

    List listUser(Map parMap);

    int delUser(Integer userId);

    int addUser(Map userMap);

    int checkUser(Map userMap);

    void addUserClient(Map userMap);

    void addUserRole(Map userMap);

    List<Map> selectAllClient();

    List listUserClient(Map parMap);

    List<Map> listUserClientRole(Map parMap);

    int updateUser(Map userMap);

    void delUserClient(Map userMap);

    void delUserRole(Map userMap);

    int checkPhone(Map userMap);

    Map selUser(Map parMap);

    int updateUserPassWord(Map parMap);

    List<Map> selOffices();

    List<Map> selUserOffice(Map parMap);

    int delUserOffice(Map parMap);

    int addUserOffice(Map parMap);
}
