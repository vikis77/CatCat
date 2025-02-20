package com.qin.catcat.unite.popo.entity;

import java.sql.Timestamp;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;


/**
 * 坐标表
 */
@lombok.Data
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@TableName("coordinate")
@lombok.Builder
public class Coordinate {
    /**
     * 主键
     */
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;
    /**
     * 区域
     */
    private String area;
    /**
     * 猫猫ID
     */
    private Long catId;
    /**
     * 描述
     */
    private String description;
    /**
     * 纬度
     */
    private Double latitude;
    /**
     * 经度
     */
    private Double longitude;
    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Timestamp updateTime;
    /* 
     * 上传者
     */
    private String uploader;
}