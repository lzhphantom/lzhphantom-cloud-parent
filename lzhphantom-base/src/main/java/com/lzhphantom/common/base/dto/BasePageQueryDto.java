package com.lzhphantom.common.base.dto;

import com.lzhphantom.common.base.result.PageDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 基础分页查询DTO类，用于分页查询
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BasePageQueryDto extends PageDto implements Serializable {
    private static final long serialVersionUID = 1L;
} 