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
public interface MenuMapper {


    List<Map> loadSonMenuList(Map map);

    int addMenu(Map map);

    void updateMenuHasSon(int menuPid);

    void delMenu(int menuId);

    List<Integer> findSonMenu(int menuId);

    List listMenuByClientId(String clientId);

    List listUserClientMenu(Map parMap);

    int updateMenu(Map param);
}
