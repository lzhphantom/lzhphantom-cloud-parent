package com.lzhphantom.common.web.controller;

import com.lzhphantom.common.base.dto.*;
import com.lzhphantom.common.base.result.LzhphantomPage;
import com.lzhphantom.common.base.result.LzhphantomResult;
import com.lzhphantom.common.mybatis.entity.BaseEntity;
import com.lzhphantom.common.web.service.BaseService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 基础控制器类，提供通用的CRUD操作
 *
 * @param <S> Service类型
 * @param <E> 实体类型
 * @param <C> 创建DTO类型
 * @param <U> 更新DTO类型
 * @param <Q> 分页查询DTO类型
 * @param <P> 分页结果DTO类型
 * @param <O> 详情DTO类型
 */
public abstract class BaseController<S extends BaseService<E, C, U, Q, P, O>, E extends BaseEntity, C extends BaseCreateDto,
        U extends BaseUpdateDto, Q extends BasePageQueryDto, P extends BasePageResultDto, O extends BaseOriginDto> {

    /**
     * 获取服务类实例
     *
     * @return 服务类实例
     */
    protected abstract S getService();

    /**
     * 创建记录
     *
     * @param createDto 创建DTO
     * @return 创建结果
     */
    @ApiOperation(value = "创建记录")
    @PostMapping("/create")
    public LzhphantomResult<Long> create(@RequestBody C createDto) {
        return LzhphantomResult.ok(getService().create(createDto));
    }

    /**
     * 更新记录
     *
     * @param updateDto 更新DTO
     * @return 更新结果
     */
    @ApiOperation("更新记录")
    @PostMapping("/update")
    public LzhphantomResult<Boolean> update(@RequestBody U updateDto) {
        return LzhphantomResult.ok(getService().update(updateDto));
    }

    /**
     * 根据ID删除记录
     *
     * @param id 记录ID
     * @return 删除结果
     */
    @ApiOperation("根据ID删除记录")
    @GetMapping("/delete/{id}")
    public LzhphantomResult<Boolean> delete(@PathVariable("id") Long id) {
        return LzhphantomResult.ok(getService().deleteById(id));
    }

    /**
     * 批量删除记录
     *
     * @param deleteDto 批量删除DTO
     * @return 删除结果
     */
    @ApiOperation("批量删除记录")
    @PostMapping("/batchDelete")
    public LzhphantomResult<Boolean> batchDelete(@RequestBody BatchDeleteDto<?> deleteDto) {
        return LzhphantomResult.ok(getService().batchDelete(deleteDto));
    }

    /**
     * 根据ID获取详情
     *
     * @param id 记录ID
     * @return 详情DTO
     */
    @ApiOperation("根据ID获取详情")
    @GetMapping("/getById/{id}")
    public LzhphantomResult<O> getById(@PathVariable("id") Long id) {
        return LzhphantomResult.ok(getService().getOriginById(id));
    }

    /**
     * 分页查询
     *
     * @param pageQueryDto 分页查询DTO
     * @return 分页结果
     */
    @ApiOperation("分页查询")
    @PostMapping("/getAllByPage")
    public LzhphantomResult<LzhphantomPage<P>> getAllByPage(@RequestBody Q pageQueryDto) {
        return LzhphantomResult.ok(getService().page(pageQueryDto));
    }
} 