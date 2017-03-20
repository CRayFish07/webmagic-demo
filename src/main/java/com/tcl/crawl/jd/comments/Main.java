package com.tcl.crawl.jd.comments;

import com.tcl.crawl.jd.comments.pipeline.JdbcPipeline;
import com.tcl.crawl.jd.comments.processor.JDCommentsCrawl;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tangb on 2017/3/17.
 */
public class Main {

    public static void main(String[] args) {
        // String url1 = "https://list.jd.com/list.html?cat=737,794,798&ev=5305_7188%40exbrand_2505&sort=sort_totalsales15_desc&trans=1&JL=3_%E5%93%81%E7%89%8C_TCL#J_crumbsBar";
        // String url2 = "https://list.jd.com/list.html?cat=737,794,798&ev=5305_7188%40exbrand_2505&page=2&sort=sort_totalsales15_desc&trans=1&JL=6_0_0#J_main";
        String url1 = "https://item.jd.com/4261888.html";
        // String url2 = "https://item.jd.com/10627656945.html";

        List<Pipeline> pipelines = new ArrayList<Pipeline>();
        pipelines.add(new ConsolePipeline());
        pipelines.add(new JdbcPipeline());

        Spider.create(new JDCommentsCrawl()).setPipelines(pipelines).addUrl(url1).thread(1).run();
    }

}
