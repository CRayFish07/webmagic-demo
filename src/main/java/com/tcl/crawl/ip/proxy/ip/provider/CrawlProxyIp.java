package com.tcl.crawl.ip.proxy.ip.provider;

import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.FilePipeline;

import java.io.File;
import java.io.IOException;

/**
 * Created by tangb on 2017/3/20.
 */
public class CrawlProxyIp {

    public static void main(String[] args){
        int page = 1;
        String baseUrl = "http://www.xicidaili.com/nt/";

        String basepath = System.getProperty("user.dir");
        String resultpath = basepath + "/data/webmagic/proxyip.txt";
        checkPath(resultpath);

        Spider spider = Spider.create(new CawlProxyIpProcessor()).addPipeline(new CheckProxyIpPipeline(resultpath));
        for(int i = 1 ; i <= 50 ; i ++){
            spider.addUrl(baseUrl + i);
        }
        spider.thread(1).run();
    }

    public static void checkPath(String path){
        File file = new File(path);
        File parentPath = file.getParentFile();
        if (!parentPath.exists()) {
            parentPath.mkdirs();
        }
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
