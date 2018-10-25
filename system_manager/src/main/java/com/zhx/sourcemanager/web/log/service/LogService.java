package com.zhx.sourcemanager.web.log.service;


import com.zhx.sourcemanager.common.util.HttpUtil;
import com.zhx.sourcemanager.common.util.JsonUtils;
import com.zhx.sourcemanager.web.log.mapper.LogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class LogService {
    @Autowired
    private LogMapper logMapper;

    public void saveClientLog(HttpServletRequest request,String userId){
        String url = HttpUtil.getReqUrl(request);
        String ip = HttpUtil.getIpAddr(request);
        Map reqMap = HttpUtil.getRequestPararms(request);
        Map map = new HashMap();
        map.put("url",url.length()>250?url.substring(0,250):url);
        map.put("reqStr", JsonUtils.toJson(reqMap));
        map.put("reqTime",new Date());
        map.put("repStr","");
        map.put("repTime",new Date());
        map.put("ip",ip);
        map.put("userId",userId);
        logMapper.insertOauthClientLog(map);
    }

}
