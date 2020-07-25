package com.simonhu.sourcemanager.common.util;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class HttpUtils {
    private static Logger log = LoggerFactory.getLogger(HttpUtils.class.toString());
    public final static int connectTimeout = 20000;
    private static final String UTF_8 = HTTP.UTF_8;
    public static ObjectMapper objectMapper = new ObjectMapper();

    public static CloseableHttpClient getHttpClient() {
        return HttpClients.createDefault();
//        return BaseHttpClient.getHttpclient();
    }


    /**
     * https  post请求
     * @param url
     * @param map
     * @return
     */
    public static String doPost(String url, Map map, Object _header) {
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
            if (_header != null) {
                Header[] header = (Header[]) _header;
                for (Header he : header) {
                    if ("Set-Cookie".equals(he.getName())) {
                        httpPost.addHeader(he);
                    }
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
            //设置重试次数为3次
            int i = 0;
            while (true){
                try{
                    response = httpClient.execute(httpPost);
                }catch(Exception e){
                    if(i>3){
                        break;
                    }
                    i++;
                }
                if (response!= null ) {
                    break;
                }
            }
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
        return result;
    }


    /**
     * http  get请求
     *
     * @param url
     * @param headp
     * @return
     */
    public static String doGet(String url, String params, Object headp) {
        String result = null;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response=null;
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(connectTimeout)
                .setConnectTimeout(connectTimeout)
                .setConnectionRequestTimeout(connectTimeout).build();
        try {
            HttpGet httpget = new HttpGet(url+"?"+params);
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
            response = getHttpClient().execute(httpget);
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
        return result;
    }

    /**
     * 获取ip地址
     * @param request
     * @return
     */
    public static String getIp(HttpServletRequest request){
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }


    public static String postxml(String url, String xmlStr){
        return postString(url,xmlStr);

//        //关闭
//        System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.SimpleLog");
//        System.setProperty("org.apache.commons.logging.simplelog.showdatetime", "true");
//        System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.commons.httpclient", "stdout");
//
//        //创建httpclient工具对象
//        HttpClient client = new HttpClient();
//        //创建post请求方法
//        PostMethod myPost = new PostMethod(url);
//        //设置请求超时时间
//        client.setConnectionTimeout(300*1000);
//        String responseString = null;
//        try{
//            //设置请求头部类型
//            myPost.setRequestHeader("Content-Type","text/xml");
//            myPost.setRequestHeader("charset","utf-8");
//
//            //设置请求体，即xml文本内容，注：这里写了两种方式，一种是直接获取xml内容字符串，一种是读取xml文件以流的形式
//            //myPost.setRequestBody(xmlStr);
//
//            myPost.setRequestEntity(new StringRequestEntity(xmlStr,"text/xml","utf-8"));
//            int statusCode = client.executeMethod(myPost);
//            if(statusCode == HttpStatus.SC_OK){
//                BufferedInputStream bis = new BufferedInputStream(myPost.getResponseBodyAsStream());
//                byte[] bytes = new byte[1024];
//                ByteArrayOutputStream bos = new ByteArrayOutputStream();
//                int count = 0;
//                while((count = bis.read(bytes))!= -1){
//                    bos.write(bytes, 0, count);
//                }
//                byte[] strByte = bos.toByteArray();
//                responseString = new String(strByte,0,strByte.length,"utf-8");
//                bos.close();
//                bis.close();
//            }
//        }catch (Exception e) {
//            e.printStackTrace();
//        }
//        myPost.releaseConnection();
//        System.out.println("responsetXmlString="+responseString);
//        return responseString;
    }

    public static String postString(String url, String param) {
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
                    .setSocketTimeout(connectTimeout)
                    .setConnectTimeout(connectTimeout)
                    .setConnectionRequestTimeout(connectTimeout).build();
            post.setConfig(requestConfig);
            HttpEntity entiry = new StringEntity(param,UTF_8);
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
            exception.printStackTrace();
            log.error("远程请求错误",exception);
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
                msg = EntityUtils.toString(entity, UTF_8);
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
