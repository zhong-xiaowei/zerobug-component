package cn.com.zerobug.component.orm;

import cn.com.zerobug.common.base.api.PageQuery;
import cn.com.zerobug.common.base.api.PageResult;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @author zhongxiaowei
 * @date 2022/3/12
 */
public class ServiceImplPro<M extends MapperPro<T>, T> extends ServiceImpl<M, T> implements IServicePro<T> {

    @Override
    public PageResult<T> queryPage(PageQuery<T> pageQuery) {
        throw new UnsupportedOperationException();
    }
}
