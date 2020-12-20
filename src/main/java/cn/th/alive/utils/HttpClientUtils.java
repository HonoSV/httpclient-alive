package cn.th.alive.utils;

import org.apache.http.Header;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HttpClientUtils {
    private static final String ENCODING = "UTF-8";
    private static final int CONNECT_TIMEOUT = 60*1000;
    private static final int SOCKET_TIMEOUT = 60*1000;
    private static final int CONNECT_REQUEST_TIMEOUT = 60*1000;
    private static final RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(CONNECT_TIMEOUT)
            .setConnectionRequestTimeout(CONNECT_REQUEST_TIMEOUT)
            .setSocketTimeout(SOCKET_TIMEOUT)
            .build();

    public static HttpClientResult doGet(String url, Map<String, String> headers, Map<String, String> params) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse httpResponse = null;

        try {
            URIBuilder uriBuilder = new URIBuilder(url);
            if (null != params) {
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    uriBuilder.setParameter(entry.getKey(), entry.getValue());
                }
            }

            HttpGet httpGet = new HttpGet(uriBuilder.build());
            httpGet.setConfig(requestConfig);
            packageHeader(headers, httpGet);
            httpResponse = httpClient.execute(httpGet);
            return getHttpClientResult(httpResponse);
        } catch (Exception e) {
            e.printStackTrace();


            
        } finally {
            release(httpResponse, httpClient);
        }
        return new HttpClientResult(HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }

    public static HttpClientResult doPost(String url, Map<String, String> headers, Map<String, String> params) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse httpResponse = null;
        try {
            HttpPost httpPost = new HttpPost(url);
            httpPost.setConfig(requestConfig);
            packageHeader(headers, httpPost);
            packageParams(params, httpPost);
            httpResponse = httpClient.execute(httpPost);
            return getHttpClientResult(httpResponse);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            release(httpResponse, httpClient);
        }
        return new HttpClientResult(HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }

    private static void packageHeader(Map<String, String> headers, HttpRequestBase httpMethod) {
        if (null != headers) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                httpMethod.setHeader(entry.getKey(), entry.getValue());
            }
        }
    }

    private static void packageParams(Map<String, String> params, HttpEntityEnclosingRequestBase httpMethod) throws UnsupportedEncodingException {
        if (null != params) {
            List<NameValuePair> dict = new ArrayList<>();
            for (Map.Entry<String, String> entry : params.entrySet()) {
                dict.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
            httpMethod.setEntity(new UrlEncodedFormEntity(dict, ENCODING));
        }
    }

    public static void release( CloseableHttpResponse httpResponse, CloseableHttpClient httpClient) {
        if (null != httpResponse) {
            try {
                httpResponse.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (null != httpClient) {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static HttpClientResult getHttpClientResult(CloseableHttpResponse httpResponse) throws IOException {
        if (null != httpResponse && null != httpResponse.getStatusLine()) {
            Header[] header = null;
            String content = "";
            if (null != httpResponse.getAllHeaders())
                header = httpResponse.getAllHeaders();
            if (null != httpResponse.getEntity())
                content = EntityUtils.toString(httpResponse.getEntity(), ENCODING);
            return new HttpClientResult(httpResponse.getStatusLine().getStatusCode(), header, content);
        }
        return new HttpClientResult(HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }
}

