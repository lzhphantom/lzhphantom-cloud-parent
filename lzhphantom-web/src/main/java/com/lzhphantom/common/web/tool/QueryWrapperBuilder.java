package com.lzhphantom.common.web.tool;


import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lzhphantom.common.base.result.PageDto;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class QueryWrapperBuilder {

    /**
     * 构建查询包装器
     * 根据提供的类和分页DTO动态生成查询条件和排序条件
     *
     * @param clazz   实体类的类型，用于创建QueryWrapper和解析列名
     * @param pageDto 包含搜索参数和排序信息的分页DTO
     * @return 返回一个配置好的QueryWrapper实例
     */
    public static <T> QueryWrapper<T> buildWrapper(Class<T> clazz, PageDto pageDto) {
        QueryWrapper<T> wrapper = new QueryWrapper<>();

        // 动态添加查询条件
        Map<String, Object> searchParams = pageDto.getSearchParams();
        if (searchParams != null && !searchParams.isEmpty()) {
            searchParams.forEach((key, value) -> {
                if (value != null) {
                    // 使用字段名获取查询条件
                    String column = getColumnName(key);
                    if (value instanceof Collection<?>) {
                        List<?> list = (List<?>) value;
                        if (!list.isEmpty()) {
                            if (column.contains("create") && list.size() == 2) {
                                wrapper.between(column, list.get(0), list.get(1));
                            } else {
                                wrapper.in(column, (Collection<?>) value);
                            }
                        }

                    } else if (value.getClass().isArray()) {
                        Object[] arr = (Object[]) value;
                        if (arr.length > 0) {
                            if (column.contains("create") && arr.length == 2) {
                                wrapper.between(column, arr[0], arr[1]);
                            } else {
                                wrapper.in(column, (Object[]) value);
                            }
                        }

                    } else if (value instanceof String) {
                        String valueStr = (String) value;
                        if (StrUtil.isNotEmpty(valueStr)) {
                            if (column.contains("_like")) {
                                column = column.replace("_like", "");
                                wrapper.like(column, value);
                            } else {
                                wrapper.eq(column, value);
                            }
                        }
                    } else {
                        if (ObjUtil.isNotNull(value)) {
                            wrapper.eq(column, value);
                        }
                    }
                }
            });
        }

        // 动态添加排序条件
        String orderBy = pageDto.getOrderBy();
        String orderType = pageDto.getOrderType();
        if (orderBy != null && !orderBy.isEmpty()) {
            if ("asc".equalsIgnoreCase(orderType)) {
                wrapper.orderByAsc(orderBy);
            } else if ("desc".equalsIgnoreCase(orderType)) {
                wrapper.orderByDesc(orderBy);
            }
        }

        return wrapper;
    }

    /**
     * 动态获取类的字段名称对应的getter方法
     *
     * @param fieldName 字段名
     * @param <T>       泛型类型
     * @return 返回字段对应的getter方法的字段名
     */
    private static <T> String getColumnName(String fieldName) {
        return camelToSnake(fieldName);
    }

    /**
     * 将驼峰命名的字符串转换为下划线分隔的字符串
     *
     * @param camelCase 驼峰命名的字符串（如 "roleName"）
     * @return 下划线分隔的字符串（如 "role_name"）
     */
    public static String camelToSnake(String camelCase) {
        if (camelCase == null || camelCase.isEmpty()) {
            return camelCase;
        }

        // 使用正则表达式匹配大写字母，并在前面加上下划线
        String snakeCase = camelCase.replaceAll("([A-Z])", "_$1").toLowerCase();

        // 如果字符串以大写字母开头，会在前面多出一个下划线，需要去掉
        if (snakeCase.startsWith("_")) {
            snakeCase = snakeCase.substring(1);
        }

        return snakeCase;
    }

    /**
     * 首字母大写
     *
     * @param fieldName 字段名
     * @return 返回首字母大写的字段名
     */
    private static String capitalize(String fieldName) {
        return Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
    }

}
