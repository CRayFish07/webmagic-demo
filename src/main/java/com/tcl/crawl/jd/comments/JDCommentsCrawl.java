package com.tcl.crawl.jd.comments;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Joiner;
import com.tcl.crawl.jd.comments.util.DoneProduct;
import com.tcl.crawl.jd.comments.util.UrlEnum;
import com.tcl.crawl.jd.comments.util.UrlUtil;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Json;

public class JDCommentsCrawl implements PageProcessor {

    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000);

    public Site getSite() {
        return site;
    }

    public void process(Page page) {
        String url = page.getUrl().toString();
        // if 页面是商品列表页，商品列表页处理
        if(url.startsWith(UrlEnum.PRODUCT_LIST.getUrl())){
            processProductList(page);
        }
        // if 页面是商品详情页，商品详情页处理
        if(url.startsWith(UrlEnum.PRODUCT_ITEM.getUrl())){
            processProductItem(page);
        }
        // if 页面是评论返回json数据，评论数据处理
        if(url.startsWith(UrlEnum.PRODUCT_COMMENT.getUrl())){
            processProductComments(page);
        }
        // if 页面是回复返回json数据，回复数据处理
        if(url.startsWith(UrlEnum.PRODUCT_REPLY.getUrl())){
            processProductReply(page);
        }
    }


    /**
     * 处理商品列表页面
     * @param page
     */
    private void  processProductList(Page page){
        Html html = page.getHtml();
        List<String> items = html.xpath("//*[@id='plist']/ul/li[@class='gl-item']//div[@class='p-img']/a/@href").all();
        if (!items.isEmpty()) {
            for (String producrUrl : items) {
                String productId = getProductId(producrUrl);
                if (DoneProduct.isDone(productId)) {
                    DoneProduct.add(productId);
                } else {
                    // 种子生成：从商品列表页中提取商品详情页的连接数据，放入爬虫
                    page.addTargetRequest(new Request(producrUrl));
                }
            }
        }
    }

    /**
     * 如果页面是商品详情页，解析出详情页的相关数据
     * @param page
     */
    private void processProductItem(Page page){
        Html html = page.getHtml();
        String productId = html.xpath("//*[@id='preview']/div[@class='preview-info']/div[@class='left-btns']/a[1]/@data-id").toString();
        System.out.println("productId:" + productId);

        String shopName = html.xpath("//*[@id=\"crumb-wrap\"]/div/div[2]/div/div[1]/div/a/text()").toString();
        System.out.println("shopName:" + shopName);

        String skuName = html.xpath("//div[@class='itemInfo-wrap']/div[@class='sku-name']/text()").toString();
        System.out.println("skuName:" + skuName);

        String selectedSku = html.xpath("//*[@id=\"choose-attr-2\"]/div[@class='dd']/div[@class='selected']/a/text()").toString();
        System.out.println("selectedSku:" + selectedSku);

        List<String> dataSkus = html.xpath("//*[@id=\"choose-attr-2\"]/div[@class='dd']/div/@data-sku").all();
        // 种子生成：该版本的商品如果没有爬取，放入爬虫进行爬取
        for (String dataSku : dataSkus) {
            System.out.println("dataSku:" + dataSku);
            if (!DoneProduct.isDone(dataSku)) {
                String dataSkuUrl = UrlUtil.getProductUrlByProductId(dataSku);
                System.out.println(dataSkuUrl);
                page.addTargetRequest(dataSkuUrl);
            }
        }

        // 种子生成：提取该详情页的评论URL，放入爬虫进行爬取
        String commentsUrl = UrlUtil.getProductCommentsUrl(productId);
        page.addTargetRequest(commentsUrl);
    }

    /**
     * 如果页面是商品评论json数据
     * @param page
     */
    private void processProductComments(Page page) {
        Json json = page.getJson();
        System.out.println(json);
        // TODO: 现阶段一次对全部的数据进行请求，后续需要修改为增量请求评论数据
        // 种子生成：获取第一次评论后得到maxPage，然后拼接出所有的评论连接，放入爬虫进行爬取
        String jsonStr = json.toString();
        JSONObject commentJsonObj = JSONObject.parseObject(jsonStr);
        Integer maxPage = commentJsonObj.getInteger("maxPage");
        String productId = commentJsonObj.getJSONObject("productCommentSummary").getString("skuId");
        if(maxPage == null || (maxPage - 0 == 0)){
            return;
        }else{
            for(int pageNum = 1; pageNum <= maxPage; pageNum++){
                page.addTargetRequest(UrlUtil.getProductCommentsUrl(productId, String.valueOf(pageNum)));
            }
        }
        JSONArray comments = commentJsonObj.getJSONArray("comments");
        for(int i = 0; i < comments.size(); i++){
            JSONObject comment = comments.getJSONObject(i);
            String guid = comment.getString("guid");
            Integer replyCount = comment.getInteger("replyCount");
            String referenceId = comment.getString("referenceId");
            if(replyCount > 0){
                int pageNum = replyCount / 15 + 1;
                for (int j = 1; j <= pageNum; j++){
                    page.addTargetRequest(UrlUtil.getProductReplyUrl(referenceId, guid, j));
                }
            }
        }

        // TODO: 波哥的解析函数放在此处
    }

    /**
     * 处理回复页面
     * @param page
     */
    private void processProductReply(Page page){
        Html html = page.getHtml();
        List<String> replyItems = html.xpath("//div[@class='reply-items']/div[@class='item']").all();
        for (String replyItem : replyItems){
            String content = replyItem.replaceAll("\\<.*?>", "").replaceAll("(?m)^\\s*$(\\n|\\r\\n)", "");
            System.out.println(content);
        }
    }

    private String getProductId(String url) {
        String productId = null;
        if (url.startsWith("https://item.jd.com/")) {
            String subUrl = url.replace("https://item.jd.com/", "");
            productId = subUrl.substring(0, subUrl.indexOf("."));
        }
        return productId;
    }

}
