package com.lzhphantom.common.web.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lzhphantom.common.base.dto.*;
import com.lzhphantom.common.base.exception.ThrowUtils;
import com.lzhphantom.common.base.result.LzhphantomPage;
import com.lzhphantom.common.base.result.ResultCode;
import com.lzhphantom.common.base.entity.BaseEntity;
import com.lzhphantom.common.web.service.BaseService;
import com.lzhphantom.common.web.tool.QueryWrapperBuilder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 基础Service实现类，实现常用的CRUD方法
 *
 * @param <M> Mapper类型
 * @param <E> 实体类型
 * @param <C> 创建DTO类型
 * @param <U> 更新DTO类型
 * @param <P> 分页查询DTO类型
 * @param <O> 详情DTO类型
 */
public abstract class BaseServiceImpl<M extends BaseMapper<E>, E extends BaseEntity, C extends BaseCreateDto, U extends BaseUpdateDto,
        Q extends BasePageQueryDto, P extends BasePageResultDto, O extends BaseOriginDto>
        extends ServiceImpl<M, E> implements BaseService<E, C, U, Q, P, O> {

    /**
     * 创建实体
     *
     * @param createDto 创建DTO
     * @return 新创建记录的ID
     */
    @Override
    @Transactional
    public Long create(C createDto) {
        E entity = createEntity(createDto);
        return entity.getId();
    }

    /**
     * 更新实体
     *
     * @param updateDto 更新DTO
     * @return 是否成功
     */
    @Override
    @Transactional
    public Boolean update(U updateDto) {
        ThrowUtils.throwIf(ObjUtil.isNull(updateDto) || ObjUtil.isNull(updateDto.getId()), ResultCode.SYSTEM_EXECUTION_ERROR, "参数错误");
        boolean exists = lambdaQuery().eq(E::getId,updateDto.getId()).exists();
        ThrowUtils.throwIf(!exists, ResultCode.NOT_FOUND_ERROR, "记录不存在");
        return updateEntity(updateDto);
    }

    /**
     * 根据ID删除实体
     *
     * @param id 记录ID
     * @return 是否成功
     */
    @Override
    @Transactional
    public Boolean deleteById(Long id) {
        ThrowUtils.throwIf(ObjUtil.isNull(id), ResultCode.SYSTEM_EXECUTION_ERROR, "参数错误");
        boolean exists = lambdaQuery().eq(E::getId,id).exists();
        ThrowUtils.throwIf(!exists, ResultCode.NOT_FOUND_ERROR, "记录不存在");
        deleteProcess(id);
        return removeById(id);
    }

    /**
     * 批量删除实体
     *
     * @param deleteDto 批量删除DTO
     * @return 是否成功
     */
    @Override
    @Transactional
    public Boolean batchDelete(BatchDeleteDto<?> deleteDto) {
        ThrowUtils.throwIf(ObjUtil.isNull(deleteDto) || CollUtil.isEmpty(deleteDto.getIds()), ResultCode.SYSTEM_EXECUTION_ERROR, "参数错误");
        Long count = lambdaQuery().in(E::getId, deleteDto.getIds()).count();
        ThrowUtils.throwIf(count != deleteDto.getIds().size(), ResultCode.NOT_FOUND_ERROR, "记录不存在");
        batchDeleteProcess(deleteDto.getIds());
        return removeByIds(deleteDto.getIds());
    }

    /**
     * 根据ID获取详情
     *
     * @param id 记录ID
     * @return 详情DTO
     */
    @Override
    public O getOriginById(Long id) {
        ThrowUtils.throwIf(ObjUtil.isNull(id), ResultCode.SYSTEM_EXECUTION_ERROR, "参数错误");
        boolean exists = lambdaQuery().eq(E::getId, id).exists();
        ThrowUtils.throwIf(!exists, ResultCode.NOT_FOUND_ERROR, "记录不存在");
        E entity = getById(id);
        return convertToOriginDto(entity);
    }

    /**
     * 分页查询
     *
     * @param pageQueryDto 分页查询DTO
     * @return 分页结果
     */
    @Override
    public LzhphantomPage<P> page(Q pageQueryDto) {
        // 构建查询条件
        QueryWrapper<E> wrapper = QueryWrapperBuilder.buildWrapper(getEntityClass(), pageQueryDto);

        // 执行分页查询
        Page<E> page = new Page<>(pageQueryDto.getCurrent(), pageQueryDto.getSize());
        Page<E> resultPage = page(page, wrapper);

        // 转换结果
        List<P> records = resultPage.getRecords().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        doPageDtoProcess(records);
        // 构建返回结果
        LzhphantomPage<P> result = new LzhphantomPage<>();
        result.setTotal(resultPage.getTotal());
        result.setRecords(records);
        return result;
    }



    /**
     * 将创建DTO转换为实体
     *
     * @param createDto 创建DTO
     * @return 实体
     */
    public abstract E createEntity(C createDto);

    /**
     * 更新实体
     *
     * @param updateDto 更新DTO
     */
    public abstract boolean updateEntity(U updateDto);

    /**
     * 将实体转换为详情DTO
     *
     * @param entity 实体
     * @return 详情DTO
     */
    public abstract O convertToOriginDto(E entity);

    public abstract P convertToDto(E entity);

    public void deleteProcess(Long id){
    }
    public void batchDeleteProcess(List<?> ids) {
    }

    public void doPageDtoProcess(List<P> records){

    }
}