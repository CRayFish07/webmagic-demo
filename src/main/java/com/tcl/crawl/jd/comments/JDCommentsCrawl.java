package com.tcl.crawl.jd.comments;

import java.util.List;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

public class JDCommentsCrawl implements PageProcessor {

    private Site site = Site.me().setRetryTimes(3).setSleepTime(100);

    public Site getSite() {
        return site;
    }

    public void process(Page page) {
        Html html = page.getHtml();
        List<String> items = html.xpath("//*[@id='plist']/ul/li[@class='gl-item']//div[@class='p-img']/a/@href").all();
        if (!items.isEmpty()) {
            for (String itemA : items) {
                System.out.println(itemA);

                page.addTargetRequest(new Request(itemA));
            }
        }

        System.out.println(html);
    }

}
