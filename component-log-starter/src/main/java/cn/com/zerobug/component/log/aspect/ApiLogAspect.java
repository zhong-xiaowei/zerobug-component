package cn.com.zerobug.component.log.aspect;

import cn.com.zerobug.common.base.api.ApiResult;
import cn.com.zerobug.common.context.GlobalContextHolder;
import cn.com.zerobug.common.exception.GeneralException;
import cn.com.zerobug.common.utils.JacksonUtil;
import cn.com.zerobug.common.utils.ServletUtil;
import cn.com.zerobug.component.log.annotation.ApiLog;
import cn.hutool.core.map.MapUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.com.zerobug.common.utils.ExceptionUtil;
import cn.com.zerobug.component.log.event.ApiLogEvent;
import cn.com.zerobug.component.log.model.LogOperateDTO;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.IntStream;

/**
 * API 接口日志
 *
 * @author zhongxiaowei
 * @date 2022-04-06
 */
@Aspect
@Slf4j
public class ApiLogAspect {


    /**
     * 切入点
     */
    @Pointcut("@annotation(cn.com.zerobug.component.log.annotation.ApiLog)")
    public void apiLogPointcut() {
    }

    /**
     * 环绕通知
     *
     * @param joinPoint
     * @return
     */
    @Around("@annotation(cn.com.zerobug.component.log.annotation.ApiLog)")
    public Object around(ProceedingJoinPoint joinPoint) {
        return ExceptionUtil.recordError(() -> {
            // 方法执行前进行预处理
            Object get = pretreatment(joinPoint, extractAnnotation(joinPoint)).apply(joinPoint).get();
            if (get instanceof Throwable) {
                throw new GeneralException((Throwable) get);
            }
            return get;
        }, "Api接口日志记录失败...");
    }

    /**
     * 方法预处理
     *
     * @param joinPoint
     * @param apiLog
     * @return 返回一个函数方法
     */
    private Function<ProceedingJoinPoint, Supplier<Object>> pretreatment(ProceedingJoinPoint joinPoint, ApiLog apiLog) {
        LogOperateDTO.LogOperateDTOBuilder builder = LogOperateDTO.builder();
        buildOtherInfo(builder, apiLog);
        buildClassAndMethodInfo(builder, apiLog, joinPoint);
        buildRequestInfo(builder);
        return proceedingJoinPoint -> () -> {
            try {
                return postProcessing(builder, apiLog, joinPoint.proceed());
            } catch (Throwable e) {
                return e;
            }
        };
    }

    /**
     * 方法后置处理
     *
     * @param builder
     * @param apiLog
     * @param result
     * @return
     */
    private Object postProcessing(final LogOperateDTO.LogOperateDTOBuilder builder, final ApiLog apiLog, final Object result) {
        builder.duration(Long.valueOf(System.currentTimeMillis() - builder.build().getStartTime().getTime()).intValue());
        buildResultInfo(builder, result, apiLog);
        SpringUtil.publishEvent(new ApiLogEvent(builder.build()));
        return result;
    }

    /**
     * 提取 APILOG 注解
     *
     * @param joinPoint
     * @return ApiLog {@link ApiLog}
     */
    private ApiLog extractAnnotation(JoinPoint joinPoint) {
        try {
            ApiLog    annotation = null;
            Signature signature  = joinPoint.getSignature();
            if (signature instanceof MethodSignature) {
                Method method = ((MethodSignature) signature).getMethod();
                if (method != null) {
                    annotation = method.getAnnotation(ApiLog.class);
                }
            }
            if (annotation == null) {
                throw new GeneralException("获取 @ApiLog 失败 ApiLogAspect.extractAnnotation() - annotation is null");
            }
            return annotation;
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

    private static void buildOtherInfo(LogOperateDTO.LogOperateDTOBuilder builder, ApiLog apiLog) {
        builder.startTime(new Date())
                .description(apiLog.description())
                .userId(GlobalContextHolder.getUserId())
                .traceId(GlobalContextHolder.getTraceId());
    }

    private static void buildClassAndMethodInfo(LogOperateDTO.LogOperateDTOBuilder builder, ApiLog apiLog, ProceedingJoinPoint joinPoint) {
        builder.javaClassPath(joinPoint.getTarget().getClass().getName())
                .javaMethod(joinPoint.getSignature().getName());
        if (apiLog.methodArgs()) {
            builder.javaMethodArgs(obtainMethodArgs(joinPoint));
        }
    }

    private static void buildRequestInfo(LogOperateDTO.LogOperateDTOBuilder builder) {
        HttpServletRequest request = ServletUtil.getRequest();
        builder.requestIp(ServletUtil.getRemoteAddress(request))
                .requestMethod(request.getMethod())
                .requestUrl(request.getRequestURI())
                .requsetUserAgent(request.getHeader("User-Agent"));
    }

    private static void buildResultInfo(LogOperateDTO.LogOperateDTOBuilder builder, Object result, ApiLog apiLog) {
        ApiResult apiResult = null;
        if (result instanceof ApiResult) {
            apiResult = (ApiResult) result;
        }
        if (apiResult != null) {
            if (apiLog.resultData()) {
                builder.resultData(JacksonUtil.toJsonString(apiResult.getData()));
            }
            builder.resultCode(apiResult.getCode()).resultMsg(apiResult.getMsg());
        }
    }

    /**
     * 获取方法参数
     *
     * @param joinPoint
     * @return
     */
    private static String obtainMethodArgs(ProceedingJoinPoint joinPoint) {
        MethodSignature     methodSignature = (MethodSignature) joinPoint.getSignature();
        String[]            argNames        = methodSignature.getParameterNames();
        Object[]            argValues       = joinPoint.getArgs();
        Map<String, Object> args            = MapUtil.newHashMap(argValues.length);
        for (int i = 0; i < argNames.length; i++) {
            String argName  = argNames[i];
            Object argValue = argValues[i];
            args.put(argName, !isIgnoreParams(argValue) ? argValue : "[ignore]");
        }
        return JacksonUtil.toJsonString(args);
    }

    /**
     * 判断是否忽略参数
     *
     * @param object
     * @return
     */
    private static boolean isIgnoreParams(Object object) {
        if (object == null) {
            return false;
        }
        Class<?> clazz = object.getClass();
        if (clazz.isArray()) {
            return IntStream.range(0, Array.getLength(object))
                    .anyMatch(index -> isIgnoreParams(Array.get(object, index)));
        }
        if (Collection.class.isAssignableFrom(clazz)) {
            return ((Collection<?>) object).stream()
                    .anyMatch((Predicate<Object>) ApiLogAspect::isIgnoreParams);
        }
        if (Map.class.isAssignableFrom(clazz)) {
            return isIgnoreParams(((Map<?, ?>) object).values());
        }
        return object instanceof MultipartFile
                || object instanceof HttpServletRequest
                || object instanceof HttpServletResponse
                || object instanceof BindingResult;
    }

}
