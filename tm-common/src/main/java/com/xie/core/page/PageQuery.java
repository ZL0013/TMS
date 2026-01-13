package com.xie.core.page;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.util.StringUtils;

/**
 * 分页查询参数
 * @param <T> 查询参数
 */
@Data
@SuppressWarnings("unused")
@Schema(description = "分页查询参数")
public class PageQuery<T> {

    @Schema(description = "页码", example = "1")
    private Integer pageNum = 1;

    @Schema(description = "每页大小", example = "10")
    private Integer pageSize = 10;

    @Schema(description = "排序字段", example = "createTime")
    private String orderBy;

    @Schema(description = "排序方向：ASC/DESC", example = "DESC")
    private String orderDirection = "DESC";

    @Schema(description = "查询条件")
    private T query;

    /**
     * 转换为 MyBatis Plus 的 Page 对象
     * @param <E> 实体类型
     * @return Page 对象
     */
    public <E> Page<E> toPage() {
        // 创建分页对象
        Page<E> page = new Page<>(pageNum, pageSize);
        
        // 如果指定了排序字段，则添加排序
        if (StringUtils.hasText(orderBy)) {
            // 判断排序方向
            boolean isAsc = "ASC".equalsIgnoreCase(orderDirection);
            // 将驼峰命名转换为下划线命名（如：createTime -> create_time）
            String columnName = camelToUnderscore(orderBy);
            // 设置排序
            page.addOrder(isAsc ? 
                OrderItem.asc(columnName) :
                OrderItem.desc(columnName));
        }
        
        return page;
    }

    /**
     * 驼峰命名转下划线命名
     * @param camelCase 驼峰命名字符串
     * @return 下划线命名字符串
     */
    private String camelToUnderscore(String camelCase) {
        if (camelCase == null || camelCase.isEmpty()) {
            return camelCase;
        }
        // 使用正则表达式将大写字母前添加下划线，并转为小写
        return camelCase.replaceAll("([A-Z])", "_$1").toLowerCase();
    }

}
