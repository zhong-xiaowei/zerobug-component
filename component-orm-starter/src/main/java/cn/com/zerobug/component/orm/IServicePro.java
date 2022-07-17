package cn.com.zerobug.component.orm;

import cn.com.zerobug.common.base.api.PageQuery;
import cn.com.zerobug.common.base.api.PageResult;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author zhongxiaowei
 * @version V1.0
 * @date 2021/9/29 1:57 下午
 */
public interface IServicePro<T> extends IService<T> {

    /**
     * 翻页查询
     *
     * @param pageQuery 翻页参数对象
     * @return PageResult 分页返回对象
     */
    PageResult<T> queryPage(PageQuery<T> pageQuery);

}
