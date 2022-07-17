package cn.com.zerobug.component.translate;

import cn.com.zerobug.common.exception.GeneralException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;

/**
 * @author zhongxiaowei
 * @date 2022/4/16
 */
@Slf4j
public abstract class AbstractDataTranslator implements DataTranslator {

    @Override
    public <T> void translation(TranslateObjectMetadata metadata, T target) {
        Object data = collectData(metadata, target, getFieldValue(metadata, target));
        if (StringUtils.hasLength(metadata.getTargetName())) {
            // if the targetName is not empty, then set the targetName field value
            this.setTargetFieldValue(target, metadata.getTargetName(), data);
            return;
        }
        throw new GeneralException("Missing " + metadata.getTargetName() + " field");
    }

    /**
     * get the field value
     *
     * @param metadata translate object metadata {@link TranslateObjectMetadata}
     * @param target   target object
     * @param <T>      target object type
     * @return field value
     */
    private <T> Object getFieldValue(TranslateObjectMetadata metadata, T target) {
        try {
            Field field = target.getClass().getDeclaredField(metadata.getFieldName());
            field.setAccessible(true);
            return field.get(target);
        } catch (NoSuchFieldException e) {
            log.error("The " + metadata.getFieldName() + " field was not found in the translation process", e);
        } catch (IllegalAccessException e) {
            log.error("The " + metadata.getFieldName() + " field cannot be accessed", e);
        }
        throw new GeneralException("get " + metadata.getFieldName() + " field value error");
    }

    /**
     * collect data from source
     *
     * @param metadata   translate object metadata {@link TranslateObjectMetadata}
     * @param source     source object
     * @param fieldValue source field value
     * @return data
     */
    protected abstract Object collectData(TranslateObjectMetadata metadata, Object source, Object fieldValue);

    /**
     * set the target field value
     *
     * @param target    target object
     * @param fieldName target field name
     * @param value     target field value
     * @param <T>       target object type
     */
    private <T> void setTargetFieldValue(T target, String fieldName, Object value) {
        try {
            Class<?> clazz = target.getClass();
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (NoSuchFieldException e) {
            log.error("The " + fieldName + " field was not found in the translation process", e);
        } catch (IllegalAccessException e) {
            log.error("The " + fieldName + " field cannot be accessed", e);
        }
    }
}
