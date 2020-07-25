package com.simonhu.oauth2.common.shortmessage.mapper;


import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

@Mapper
public interface ShortMessageMapper {

    int updatePhoneMessage(Map parmap);
    int insertPhoneMessage(Map parmap);
}
