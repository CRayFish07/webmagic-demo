package com.tcl.crawl.jd.comments.dao;

import com.tcl.crawl.jd.comments.model.Product;
import com.tcl.crawl.jd.comments.util.DBUtilsHelper;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ArrayListHandler;
import org.apache.log4j.Logger;
import redis.clients.jedis.params.Params;

import java.sql.SQLException;
import java.util.UUID;

/**
 * Created by tangb on 2017/3/18.
 */
public class ProductDao extends AbstractDao<Product>{

    private Logger log = Logger.getLogger(this.getClass());

    DBUtilsHelper dbh = new DBUtilsHelper();

    public void insert(Product product) {
        if(product == null){
            log.warn("商品对象为null");
            return;
        }
        String insertSql = "insert into `crawl_comments`.`tbl_jd_product`(`id`,`shop_name`,`sku_name`,`sku_url`,`sku_version`,`insert_time`,`good_rate`,`good_rate_tag`,`product_id`)values(?,?,?,?,?,?,?,?,?)";
        QueryRunner run = dbh.getRunner();
        Object[] params = {UUID.randomUUID().toString(), product.getShopName(), product.getSkuName(), product.getSkuUrl(), product.getSkuVersion(), product.getInsertTime(), product.getGoodRate(), product.getGoodRateTag(), product.getProductId()};
        try {
            run.insert(insertSql, new ArrayListHandler(), params);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateGoodRate(String productId, Double goodRate, String goodRateTag){
        String updateSql = "update `crawl_comments`.`tbl_jd_product` set good_rate = ?, good_rate_tag = ? where product_id = ? " +
                "and insert_time = (select max(insert_time) from `crawl_comments`.`tbl_jd_product` where product_id = ?)";
        QueryRunner run = dbh.getRunner();
        Object[] params = {goodRate, goodRateTag, productId, productId};
        try {
            run.update(updateSql, params);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
