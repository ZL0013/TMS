package com.xie.core.page;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 分页结果
 * @param <T> 数据类型
 */
@Data
@SuppressWarnings("unused")
@Schema(description = "分页结果")
public class PageResult<T> {

    @Schema(description = "总记录数")
    private Long total;

    @Schema(description = "当前页码")
    private Long pageNum;

    @Schema(description = "每页大小")
    private Long pageSize;

    @Schema(description = "总页数")
    private Long pages;

    @Schema(description = "数据列表")
    private List<T> records;

    /**
     * 从 MyBatis Plus 的 Page 对象构建
     * @param page MyBatis Plus Page 对象
     * @param <E> 实体类型
     * @return PageResult 对象
     */
    public static <E> PageResult<E> of(Page<E> page) {
        PageResult<E> result = new PageResult<>();
        result.setTotal(page.getTotal());
        result.setPageNum(page.getCurrent());
        result.setPageSize(page.getSize());
        result.setPages(page.getPages());
        result.setRecords(page.getRecords());
        return result;
    }

    /**
     * 从 MyBatis Plus 的 Page 对象构建，并转换数据类型
     * @param page MyBatis Plus Page 对象
     * @param converter 转换函数
     * @param <E> 源数据类型
     * @param <R> 目标数据类型
     * @return PageResult 对象
     */
    public static <E, R> PageResult<R> of(Page<E> page, Function<E, R> converter) {
        PageResult<R> result = new PageResult<>();
        result.setTotal(page.getTotal());
        result.setPageNum(page.getCurrent());
        result.setPageSize(page.getSize());
        result.setPages(page.getPages());
        result.setRecords(page.getRecords().stream()
                .map(converter)
                .collect(Collectors.toList()));
        return result;
    }
}
