package com.lzhphantom.common.web.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.lzhphantom.common.base.dto.*;
import com.lzhphantom.common.base.result.LzhphantomPage;
import com.lzhphantom.common.web.entity.BaseEntity;

/**
 * 基础Service接口，定义常用的CRUD方法
 *
 * @param <E> 实体类型
 * @param <C> 创建DTO类型
 * @param <U> 更新DTO类型
 * @param <P> 分页查询DTO类型
 * @param <O> 详情DTO类型
 */
public interface BaseService<E extends BaseEntity, C extends BaseCreateDto, U extends BaseUpdateDto, Q extends BasePageQueryDto,P extends BasePageResultDto, O extends BaseOriginDto> extends IService<E> {

    /**
     * 创建记录
     *
     * @param createDto 创建DTO
     * @return 新创建记录的ID
     */
    Long create(C createDto);

    /**
     * 更新记录
     *
     * @param updateDto 更新DTO
     * @return 是否成功
     */
    Boolean update(U updateDto);

    /**
     * 根据ID删除记录
     *
     * @param id 记录ID
     * @return 是否成功
     */
    Boolean deleteById(Long id);

    /**
     * 批量删除记录
     *
     * @param deleteDto 批量删除DTO
     * @return 是否成功
     */
    Boolean batchDelete(BatchDeleteDto<?> deleteDto);

    /**
     * 根据ID获取详情
     *
     * @param id 记录ID
     * @return 详情DTO
     */
    O getOriginById(Long id);

    /**
     * 分页查询
     *
     * @param pageQueryDto 分页查询DTO
     * @return 分页结果
     */
    LzhphantomPage<P> page(Q pageQueryDto);
}