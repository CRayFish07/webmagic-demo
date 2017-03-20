package com.tcl.crawl.ip.proxy;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by tangb on 2017/3/20.
 */
public class HttpUserAgent {

    private static List<String> userAgentList;

    static {
        userAgentList = new ArrayList<String>();
        userAgentList.add("Mozilla/5.0 (Windows NT 10.0; WOW64; rv:52.0) Gecko/20100101 Firefox/52.0");
        userAgentList.add("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36");
    }

    public static String get(){
        Random random = new Random();
        int i = Math.abs(random.nextInt()) % 2;
        return userAgentList.get(i);
    }

    public static void main(String[] args){
        int i = 0;
        while(++ i < 100){
            System.out.println(get());
        }
    }

}
