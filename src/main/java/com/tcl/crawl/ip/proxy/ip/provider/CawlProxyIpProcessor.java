package com.tcl.crawl.ip.proxy.ip.provider;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

import java.util.List;

/**
 * Created by tangb on 2017/3/20.
 */
public class CawlProxyIpProcessor implements PageProcessor {
    private Site site = Site.me()
            .setRetryTimes(2)
            .setSleepTime(10 * 1000)
            .setCharset("UTF-8")
            .setUserAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.63 Safari/537.36");
    ;

    public Site getSite() {
        return site;
    }

    public void process(Page page) {
        Html html = page.getHtml();

        List<String> proxyIps = html.xpath("//table[@id='ip_list']/tbody/tr/td[2]/text()").all();
        List<String> proxyPorts = html.xpath("//table[@id='ip_list']/tbody/tr/td[3]/text()").all();

        page.putField("proxyIps", proxyIps);
        page.putField("proxyPorts", proxyPorts);
    }

}
