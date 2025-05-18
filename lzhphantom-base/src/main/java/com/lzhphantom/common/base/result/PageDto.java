package com.lzhphantom.common.base.result;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;

/**
 * 分页查询传输对象
 * 用于封装分页查询的相关参数，以便在服务层和数据访问层之间传递
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PageDto extends BasePageDto{
    private Map<String, Object> searchParams; // 查询条件
}

