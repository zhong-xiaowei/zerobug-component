package cn.com.zerobug.component.orm.handle;

import cn.com.zerobug.common.base.dataobject.BaseDO;
import cn.com.zerobug.common.context.GlobalContextHolder;
import cn.com.zerobug.common.exception.GeneralException;
import cn.com.zerobug.common.exception.enums.BaseErrorCode;
import cn.hutool.core.exceptions.ExceptionUtil;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Objects;


/**
 * @author zhongxiaowei
 * @version V1.1
 * @date 2021/11/11 14:57
 * @updated on 2022/04/07 23:11
 */
@Slf4j
@Component
public class PopulateMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        try {
            this.strictInsertFill(metaObject, "createTime", Date.class, new Date());
            this.strictInsertFill(metaObject, "createBy", Long.class, GlobalContextHolder.getUserId());
        } catch (Exception e) {
            log.error("自动填充错误,原因：[ {} ]", ExceptionUtil.getMessage(e));
            throw new GeneralException(BaseErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        try {
            this.strictUpdateFill(metaObject, "updateTime", Date.class, new Date());
            this.strictUpdateFill(metaObject, "updateBy", Long.class, GlobalContextHolder.getUserId());
        } catch (Exception e) {
            log.error("自动填充错误,原因：[ {} ]", ExceptionUtil.getMessage(e));
            throw new GeneralException(BaseErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 填充字段
     * 对继承了BaseDO的实体类，自动填充Id,createTime、createBy、updateTime、updateBy字段
     * Id: 如果id为空，并且是 IdType.INPUT 时，自动生成id 并填充 ，填充的值为 雪花算法生成的id
     * createBy、updateBy: 全局上下文中的用户ID
     * createTime、updateTime: 当前时间
     * //TODO 等待完成
     *
     * @param metaObject
     */
    private void doInsterFill(MetaObject metaObject) {
        Object originalObject = metaObject.getOriginalObject();
        // 继承了BaseDO的实体类
        if (originalObject instanceof BaseDO) {
            BaseDO baseDO = (BaseDO) originalObject;
            if (Objects.nonNull((baseDO.getId()))) {
                this.doIdFill(metaObject, baseDO);
            }
        }
    }

    private void doIdFill(MetaObject metaObject, BaseDO baseDO) {

    }


}