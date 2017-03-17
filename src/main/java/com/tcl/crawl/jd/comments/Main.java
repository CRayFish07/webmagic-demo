package com.tcl.crawl.jd.comments;

import us.codecraft.webmagic.Spider;

/**
 * Created by tangb on 2017/3/17.
 */
public class Main {

    public static void main(String[] args) {
        // String url =
        // "https://list.jd.com/list.html?cat=737,794,798&ev=5305_7188%40exbrand_2505&sort=sort_totalsales15_desc&trans=1&JL=3_%E5%93%81%E7%89%8C_TCL#J_crumbsBar";
        String url = "https://item.jd.com/941189.html";
        Spider.create(new JDCommentsCrawl()).addUrl(url).thread(1).run();
    }

}
