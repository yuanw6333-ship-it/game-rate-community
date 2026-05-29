package com.gamerate.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("platform")
public class Platform extends BaseEntity {

    private String name;

    private String code;

    private String description;

    private Integer sortOrder;

    private Integer status;
}
