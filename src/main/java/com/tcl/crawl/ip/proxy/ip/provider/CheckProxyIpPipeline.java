package com.tcl.crawl.ip.proxy.ip.provider;

import com.tcl.crawl.ip.proxy.IPUtils;
import com.tcl.crawl.ip.proxy.ip.utils.ProxyIPPool;
import org.apache.commons.codec.digest.DigestUtils;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.io.*;
import java.util.List;
import java.util.Map;

/**
 * Created by tangb on 2017/3/20.
 */
public class CheckProxyIpPipeline implements Pipeline {

    public String resultPath = null;

    public CheckProxyIpPipeline(){
        this.resultPath = "/data/webmagic/proxyip.txt";
    }

    public CheckProxyIpPipeline(String resultPath) {
        this.resultPath = resultPath;
    }

    public void process(ResultItems resultItems, Task task) {
        PrintWriter printWriter = null;
        try {
            printWriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(new File(resultPath)), "UTF-8"));
            String url = resultItems.getRequest().getUrl();
            Map<String, Object> map = resultItems.getAll();
            List<String> proxyIps = (List<String>) map.get("proxyIps");
            List<String> proxyPorts = (List<String>) map.get("proxyPorts");
            if (proxyIps != null && proxyPorts != null && proxyIps.size() == proxyPorts.size()) {
                for (int i = 0; i < proxyIps.size(); i++) {
                    System.out.println(proxyIps.get(i) + ProxyIPPool.SPLIT + Integer.valueOf(proxyPorts.get(i)));
                    Boolean isProxy = IPUtils.checkProxyIp(proxyIps.get(i), Integer.valueOf(proxyPorts.get(i)));
                    if (isProxy) {
                        System.out.println(proxyIps.get(i) + ProxyIPPool.SPLIT + Integer.valueOf(proxyPorts.get(i)) + " is ok");
                        ProxyIPPool.add(proxyIps.get(i), Integer.valueOf(proxyPorts.get(i)));
                        printWriter.append(proxyIps.get(i) + ProxyIPPool.SPLIT + Integer.valueOf(proxyPorts.get(i)) + "\n");
                    }

                    if(i % 10 == 0){
                        printWriter.flush();
                    }
                }
                printWriter.flush();
            }
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }finally {
            if(printWriter != null){
                printWriter.flush();
                printWriter.close();
            }
        }
    }

}
