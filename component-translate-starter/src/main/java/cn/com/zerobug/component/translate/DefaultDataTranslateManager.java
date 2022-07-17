package cn.com.zerobug.component.translate;

import cn.com.zerobug.component.translate.annotation.DataTranslate;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.ClassUtil;
import cn.com.zerobug.component.translate.properties.TranslateProperties;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

/**
 * @author zhongxiaowei
 * @date 2022/4/15
 */
public class DefaultDataTranslateManager implements DataTranslateManager {

    private final TranslateProperties properties;
    private final List<DataTranslator> translators;
    private List<TranslateObjectMetadata> pool = new ArrayList<>(16);

    public DefaultDataTranslateManager(TranslateProperties properties, List<DataTranslator> translators) {
        this.properties = properties;
        this.translators = translators;
        this.initialize();
    }

    @Override
    public void initialize() {
        this.loadTranslateObjectMetadata();
    }

    @Override
    public <T> void transform(T t) {
        Class<?> aClass = t.getClass();
        for (TranslateObjectMetadata metadata : pool) {
            /**
             * TODO
             * The first version only supports current class translations,
             * The target is support super class and sub class and combin field translations
             * @author zhongxiaowei
             * @date 2022/4/15
             */
            if (metadata.getSourceClass().equals(aClass)) {
                metadata.getTranslator().translation(metadata, t);
            }
        }
    }

    protected void loadTranslateObjectMetadata() {
        Assert.notBlank(this.properties.getPackagePath(), "packagePath must not be blank");
        String[] paths = properties.getPackagePath().split(StrPool.COMMA);
        for (String path : paths) {
            // gets all class in path
            Set<Class<?>> classes = ClassUtil.scanPackage(path);
            classes.forEach(clazz ->
                    getFieldForAnnotation(clazz, field -> {
                        DataTranslate           dataTranslate = field.getAnnotation(DataTranslate.class);
                        TranslateObjectMetadata metadata      = buildTranslateObjectMetadata(clazz, field, dataTranslate);
                        pool.add(metadata);
                    })
            );
        }
    }

    /**
     * build TranslateObjectMetadata
     *
     * @param clazz
     * @param field
     * @param annotation
     * @return
     */
    private TranslateObjectMetadata buildTranslateObjectMetadata(Class<?> clazz, Field field, DataTranslate annotation) {
        TranslateObjectMetadata metadata = new TranslateObjectMetadata();
        metadata.setTranslateType(annotation.type());
        metadata.setFieldName(field.getName());
        metadata.setSourceClass(clazz);
        metadata.setTargetName(annotation.target());
        metadata.setTranslator(getTranslator(annotation.translator()));
        return metadata;
    }

    /**
     * get DataTranslator
     *
     * @param translator translator class
     * @return DataTranslator {@link DataTranslator}
     */
    private DataTranslator getTranslator(Class<? extends DataTranslator> translator) {
        for (DataTranslator dataTranslator : translators) {
            if (dataTranslator.getClass().equals(translator)) {
                return dataTranslator;
            }
        }
        throw new IllegalArgumentException("Unable to find translator: " + translator.getName());
    }

    /**
     * gets field macked by DataTranslate annotation
     *
     * @param clazz class
     * @param consumer consumer {@link Consumer}
     */
    private void getFieldForAnnotation(Class<?> clazz, Consumer<Field> consumer) {
        Arrays.stream(clazz.getDeclaredFields()).forEach(field -> {
            if (field.isAnnotationPresent(DataTranslate.class)) {
                consumer.accept(field);
            }
        });
    }


}
