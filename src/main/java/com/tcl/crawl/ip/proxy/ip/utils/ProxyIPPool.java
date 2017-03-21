package com.tcl.crawl.ip.proxy.ip.utils;

import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by tangb on 2017/3/20.
 */
public class ProxyIPPool {

    public static final String SPLIT = "::";

    private static List<String> proxyIPPool = new CopyOnWriteArrayList<String>();

    public static void add(String proxyIp, Integer proxyPort){
        proxyIPPool.add(proxyIp + SPLIT + proxyPort);
    }

    public static void remove(String proxyIp, Integer proxyPort){
        proxyIPPool.remove(proxyIp + SPLIT + proxyPort);
    }
    
    public static synchronized String getRandomProxyIp(){
        String ipInfo = null;
        if(proxyIPPool != null && proxyIPPool.size() > 0){
            int size = proxyIPPool.size();
            Random random = new Random();
            int i = random.nextInt(size);

            while (ipInfo == null){
                try {
                    ipInfo = proxyIPPool.get(i);
                }catch (Exception e){
                }
            }
        }
        return ipInfo;
    }

    public static void main(String[] args){
        int i = 0;
        while(i < 100){
            i ++;
            System.out.println(getRandomProxyIp());
        }
    }

}
