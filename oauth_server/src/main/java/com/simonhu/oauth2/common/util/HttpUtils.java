package com.simonhu.oauth2.common.util;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class HttpUtils {

    private static final Logger log = LoggerFactory.getLogger(HttpUtils.class);

    /**
     * https  post请求
     *
     * @param url
     * @param map
     * @return
     */
    public static String doPost(String url, Map map, Object headp, int connectTimeout) {
        String result = null;
        CloseableHttpResponse response=null;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(connectTimeout)
                .setConnectTimeout(connectTimeout)
                .setConnectionRequestTimeout(connectTimeout).build();
        try {
            HttpPost httpPost = new HttpPost(url);
            httpPost.setConfig(requestConfig);
            // 设置Cookie
            if (headp != null) {
                Header[] header = (Header[]) headp;
                for (Header he : header) {
//                    if ("Set-Cookie".equals(he.getName())) {
                        httpPost.addHeader(he);
//                    }
                }
            }
            //httpPost.addHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2272.118 Safari/537.36");
            //设置参数
            StringBuilder sb = new StringBuilder();
            List<NameValuePair> list = new ArrayList<NameValuePair>();
            for (Iterator it = map.keySet().iterator(); it.hasNext(); ) {
                Object key = it.next();
                list.add(new BasicNameValuePair(String.valueOf(key), String.valueOf(map.get(key))));
                sb.append(String.valueOf(key)+"="+ String.valueOf(map.get(key)));
                sb.append("*********");
            }
            if (list.size() > 0) {
                UrlEncodedFormEntity entity = null;
                entity = new UrlEncodedFormEntity(list, "UTF-8");
                httpPost.setEntity(entity);
            }
            log.info("调用外部接口地址："+url);
            log.info("调用外部接口参数："+sb.toString());
            response =httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            result = EntityUtils.toString(entity, "UTF-8");
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                if(response!=null){
                    response.close();
                }
                if(httpClient!=null){
                    httpClient.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        log.info("调用外部接口返回值："+result);
        return result;
    }


    /**
     * http  get请求
     *
     * @param url
     * @param headp
     * @return
     */
    public static String doGet(String url, Object headp, int connectTimeout) {
        String result = null;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response=null;
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(connectTimeout)
                .setConnectTimeout(connectTimeout)
                .setConnectionRequestTimeout(connectTimeout).build();
        try {
            HttpGet httpget = new HttpGet(url);
            httpget.setConfig(requestConfig);
            // 设置Cookie
            if (headp != null) {
                Header[] header11 = (Header[]) headp;
                for (Header he : header11) {
                    if ("Set-Cookie".equals(he.getName())) {
                        httpget.addHeader(he);
                    }
                }
            }
            log.info("调用外部接口地址+参数："+url);
            response = httpClient.execute(httpget);
            HttpEntity entity = response.getEntity();
            result = EntityUtils.toString(entity, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if(response!=null){
                    response.close();
                }
                if(httpClient!=null){
                    httpClient.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        log.info("调用外部接口返回值："+result);
        return result;
    }

    public static String post(String url, String param, Object head) {
        log.info("POSTURL*********************************************************************");
        log.info("POSTURL:" + url);
        log.info("POSTURL*********************************************************************");
        log.info("POST_PARAM*********************************************************************");
        log.info("POST_PARAM:" + param);
        log.info("POST_PARAM*********************************************************************");
        CloseableHttpClient client = HttpClients.createDefault();
        try {
            HttpPost post = new HttpPost(url);
            RequestConfig requestConfig = RequestConfig.custom()
                    .setSocketTimeout(10000)
                    .setConnectTimeout(10000)
                    .setConnectionRequestTimeout(10000).build();
            post.setConfig(requestConfig);
            if (head != null) {
                Header[] header = (Header[]) head;
                for (Header he : header) {
                    post.addHeader(he);
                }
            }
            HttpEntity entiry = new StringEntity(param,"UTF-8");
            post.setEntity(entiry);
            CloseableHttpResponse resonse = client.execute(post);
            try {
                String result = entityToString(resonse);
                log.info("POST_RETURN*********************************************************************");
                log.info("POST_RETURN:" + result);
                log.info("POST_RETURN*********************************************************************");
                return result;
            } finally {
                post.abort();
                resonse.close();
            }
        } catch (Exception exception) {
        } finally {
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    public static String entityToString(HttpResponse resonse) throws Exception {
        HttpEntity entity = resonse.getEntity();
        if (entity != null) {
            String msg = null;
            try {
                msg = EntityUtils.toString(entity,"UTF-8");
            } catch (IOException e) {
                e.printStackTrace();
            }
            int code = resonse.getStatusLine().getStatusCode();
            if (code == 200) {
                return msg;
            } else {
                String errerMsg = (msg == null ? null : msg);
                throw new Exception("http code:" + code + ",error:" + errerMsg);
            }
        }
        throw new Exception("http entity is null");
    }



}
