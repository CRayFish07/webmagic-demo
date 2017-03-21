package com.tcl.crawl.jd.comments.dao;

import com.tcl.crawl.jd.comments.model.Comment;
import com.tcl.crawl.jd.comments.util.DBUtilsHelper;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ArrayListHandler;
import org.apache.log4j.Logger;

import java.sql.SQLException;
import java.util.UUID;

/**
 * Created by tangb on 2017/3/18.
 */
public class CommentDao extends AbstractDao<Comment>{

    private Logger log = Logger.getLogger(this.getClass());

    DBUtilsHelper dbh = new DBUtilsHelper();

    public void insert(Comment comment) {
        if(comment == null){
            log.warn("评论对象为null");
            return;
        }
        String insertSql = "INSERT INTO `tbl_jd_comment`(`id`,`product_id`,`nick_name`,`comment_score`,`comment_level`,`user_level`,`comment`,`after_comment`,`useful_vote_count`,`reply_count`,`user_client`,`creation_time`,`insert_time`,`guid`,`url`)VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        QueryRunner run = dbh.getRunner();
        Object[] params = {UUID.randomUUID().toString(), comment.getProductId(), comment.getNickName(), comment.getScore(), comment.getCommentLevel(), comment.getUserLevel(), comment.getComment(), comment.getAfterComment(), comment.getUsefulVoteCount(), comment.getReplyCount(), comment.getUserClient(), comment.getCreationTime(), comment.getInsertTime(), comment.getGuid(), comment.getUrl()};
        try {
            run.insert(insertSql, new ArrayListHandler(), params);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
