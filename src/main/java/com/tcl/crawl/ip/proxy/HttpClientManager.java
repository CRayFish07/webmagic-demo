package com.tcl.crawl.ip.proxy;

import org.apache.http.HttpHost;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

/**
 * Created by tangb on 2017/3/20.
 */
public class HttpClientManager {

    private static HttpClient client = null;

    public static HttpClient getClient(String proxyIp, Integer proxyPort){
        if(client == null){
            synchronized (HttpClientManager.class){
                if(client == null){
                    HttpHost proxy = new HttpHost(proxyIp, proxyPort);
                    HttpClientBuilder builder = HttpClientBuilder.create().setProxy(proxy);
                    client = builder.build();
                }
            }
        }
        return client;
    }
}
