package cn.com.zerobug.component.orm;

import cn.com.zerobug.common.base.api.PageQuery;
import cn.com.zerobug.common.base.api.PageResult;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;

/**
 * @author zhongxiaowei
 * @version V1.0
 * @date 2021/9/29 1:57 下午
 */
public interface MapperPro<T> extends BaseMapper<T> {

    /**
     * 分页查询
     *
     * @param pageQuery
     * @param queryWrapper
     * @return
     */
    default PageResult<T> selectPage(PageQuery pageQuery, @Param("ew") Wrapper<T> queryWrapper) {
        Page<T> page = pageQuery.buildPage();
        selectPage(page, queryWrapper);
        return PageResult.accept(page);
    }

    /**
     * 批量插入
     *
     * @param batchList
     * @return
     */
    int batchInsert(@Param("list") Collection<T> batchList);

}
