package com.lzhphantom.common.base.result;

import lombok.Data;

import java.util.Objects;

/**
 * 分页查询传输对象
 * 用于封装分页查询的相关参数，以便在服务层和数据访问层之间传递
 */
@Data
public class BasePageDto {
    private Integer current = 1; // 当前页
    private Integer size = 10; // 每页大小
    private String orderBy; // 排序字段
    private String orderType; // 排序方向：asc 或 desc

    /**
     * 检查分页对象是否为空
     * 当当前页和每页大小都未被设置时，认为该分页对象为空
     * 不能防止爬虫，除非加其他限制
     *
     * @return 如果当前页和每页大小都为null，则返回true，否则返回false
     */
    @Deprecated
    public boolean isEmpty() {
        return Objects.isNull(current) && Objects.isNull(size);
    }
}

