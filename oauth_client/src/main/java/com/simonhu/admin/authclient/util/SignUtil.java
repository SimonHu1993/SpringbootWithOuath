package com.simonhu.admin.authclient.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

/**
 * 供验证APP签名，
 * Created by mulder on 2015/9/24.
 */
public class SignUtil implements Comparable<SignUtil> {

    private static Logger log = LoggerFactory.getLogger(String.valueOf(SignUtil.class));

    public String s;

    public SignUtil(String s) {
        this.s = s;
    }

    /**
     * 返回服务端签名
     * @param paramMap
     * @return
     */
    public static String buildServerSign(Map paramMap, String salt){
        Iterator<Map.Entry<String, String[]>> entries = paramMap.entrySet().iterator();
        String ss[] = new String[paramMap.size()-1];
        int index = 0;
        while (entries.hasNext()) {
            Map.Entry<String, String[]> entry = entries.next();
            String key = entry.getKey();
            if ("sign".equals(key)){
                continue;
            }
            else{
                ss[index] = key;
                index++;
            }
        }
        SignUtil mySs[]=new SignUtil[ss.length];//创建自定义排序的数组
        for (int i = 0; i < ss.length; i++) {
            mySs[i]=new SignUtil(ss[i]);
        }
        StringBuilder paramBuilder = new StringBuilder();
        Arrays.sort(mySs);//排序
        for (int i = 0; i < mySs.length; i++) {
            String key = mySs[i].s;
            paramBuilder.append(key);
            paramBuilder.append("=");
            String value;
            try{
                String[] val = (String[]) paramMap.get(key);
                value = val[0];
            }catch (Exception e){
                value = String.valueOf(paramMap.get(key));
            }
            paramBuilder.append(value);
            paramBuilder.append("&");
        }
        String endParamBuffer = paramBuilder.substring(0, paramBuilder.length() - 1);
        String enCodeParam=Base64Utils.getBase64(endParamBuffer).toLowerCase().replace("=","");
        String paramSha1 = Sha1Util.encodeSha256(enCodeParam,salt );
        log.info("服务端验签:********************************************** " );
        log.info("salt: " + salt);
        log.info("接收到的参数: " + endParamBuffer);
        log.info("接收到的参数base64: " + enCodeParam);
        log.info("sha1结果: "+paramSha1);
        log.info("服务端验签:********************************************** " );
        return paramSha1;
    }


    /**
     * 返回客户端签名
     * @param paramMap
     * @param salt
     */
    public static String buildSign(Map paramMap, String salt){
        Iterator<Map.Entry<String, String>> entries = paramMap.entrySet().iterator();
        String ss[] = new String[paramMap.size()];
        int index = 0;
        while (entries.hasNext()) {
            Map.Entry<String, String> entry = entries.next();
            String key = entry.getKey();
            ss[index] = key;
            index++;
        }
        SignUtil mySs[]=new SignUtil[ss.length];//创建自定义排序的数组
        for (int i = 0; i < ss.length; i++) {
            mySs[i]=new SignUtil(ss[i]);
        }
        StringBuilder paramBuilder = new StringBuilder();
        Arrays.sort(mySs);//排序
        for (int i = 0; i < mySs.length; i++) {
            String key = mySs[i].s;
            paramBuilder.append(key);
            paramBuilder.append("=");
            try{
                String val = (String) paramMap.get(key);
                paramBuilder.append(val);
            }
            catch (Exception e){
                paramBuilder.append(paramMap.get(key));
            }
            paramBuilder.append("&");
        }
        String endParamBuffer = paramBuilder.substring(0, paramBuilder.length() - 1);
        String enCodeParam=Base64Utils.getBase64(endParamBuffer).toLowerCase().replace("=","");
        String paramSha1 = Sha1Util.encodeSha256(enCodeParam,salt );
        log.info("客户端验签:********************************************** " );
        log.info("salt: " + salt);
        log.info("接收到的参数: " + endParamBuffer);
        log.info("接收到的参数base64: " + enCodeParam);
        log.info("sha1结果: "+paramSha1);
        log.info("客户端验签:********************************************** " );
        return paramSha1;
    }

    @Override
    public int compareTo(SignUtil o) {
        if (o == null || o.s == null) return 1;
        if (s.length() > o.s.length()) return 1;
        else if (s.length() < o.s.length()) return -1;
        return s.compareTo(o.s);
    }



    public  static  void main(String[] args){

    }
}
