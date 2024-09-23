package com.qin.catcat.unite.popo.entity;

import java.sql.Timestamp;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/* 
 * 帖子表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName(value = "post")
public class Post {
    //主键ID
    @TableId(value = "post_id",type = IdType.INPUT)
    private Long postId;
    //标题
    private String title;
    //文章
    private String article;
    //作者ID
    private Long authorId;
    //点赞数
    private Integer likeCount;
    // 收藏数
    private Integer collectingCount;
    //评论数
    private Integer commentCount;
    //发帖时间
    @JsonProperty("send_time")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss.SSS")
    private Timestamp sendTime;
    //更新时间
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss.SSS")
    private Timestamp updateTime;
    //首页图片地址
    private String coverPicture;
}
