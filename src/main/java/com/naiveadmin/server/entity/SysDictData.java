package com.naiveadmin.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 字典数据实体类
 */
@Data
@TableName("sys_dict_data")
public class SysDictData implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 字典数据ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 字典类型ID
     */
    private Long dictTypeId;

    /**
     * 字典类型
     */
    private String dictType;

    /**
     * 字典标签
     */
    private String label;

    /**
     * 字典值
     */
    private String value;

    /**
     * 样式属性
     */
    private String cssClass;

    /**
     * 表格回显样式
     */
    private String listClass;

    /**
     * 是否默认（0-否，1-是）
     */
    private Boolean isDefault;

    /**
     * 状态（0-停用，1-正常）
     */
    private Integer status;

    /**
     * 排序号
     */
    private Integer orderNum;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 是否删除（0-未删除，1-已删除）
     */
    @TableLogic
    private Boolean deleted;
} 