package cn.com.zerobug.component.swagger.config;

import cn.com.zerobug.component.swagger.properties.Swagger3Properties;
import com.github.xiaoymin.knife4j.spring.extension.OpenApiExtensionResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.plugin.core.OrderAwarePluginRegistry;
import org.springframework.plugin.core.PluginRegistry;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.RequestParameterBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.RequestParameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.DocumentationPlugin;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.spring.web.plugins.DocumentationPluginsManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.StreamSupport;

import static java.util.Collections.singleton;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

/**
 * 重写 DocumentationPluginsManager 构建分组文档
 *
 * @author zhongxiaowei
 * @date 2022/4/5
 */
@Primary
@Configuration
@ConditionalOnProperty(prefix = "knife4j", name = "enable", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(Swagger3Properties.class)
@RequiredArgsConstructor
@Import({SwaggerConfiguration.class})
public class Swagger3AutoConfiguration extends DocumentationPluginsManager implements EnvironmentAware {

    private final Swagger3Properties       properties;
    private final OpenApiExtensionResolver openApiExtensionResolver;
    private       Environment              environment;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public Collection<DocumentationPlugin> documentationPlugins() throws IllegalStateException {
        List<DocumentationPlugin> plugins = this.pluginRegistry().getPlugins();
        ensureNoDuplicateGroups(plugins);
        return plugins.isEmpty() ? singleton(this.defaultDocumentationPlugin()) : plugins;
    }

    public static void ensureNoDuplicateGroups(List<DocumentationPlugin> allPlugins) throws IllegalStateException {
        Map<String, List<DocumentationPlugin>> plugins = allPlugins.stream()
                .collect(groupingBy(
                        input -> ofNullable(input.getGroupName()).orElse("default"),
                        LinkedHashMap::new,
                        toList()));
        Iterable<String> duplicateGroups = plugins.entrySet().stream()
                .filter(input -> input.getValue().size() > 1)
                .map(Map.Entry::getKey)
                .collect(toList());
        if (StreamSupport.stream(duplicateGroups.spliterator(), false).count() > 0) {
            throw new IllegalStateException(String.format("Multiple Dockets with the same group name are not supported. "
                    + "The following duplicate groups were discovered. %s", String.join(",", duplicateGroups)));
        }
    }

    private DocumentationPlugin defaultDocumentationPlugin() {
        return new Docket(DocumentationType.OAS_30);
    }

    public PluginRegistry pluginRegistry() {
        List<DocumentationPlugin>       documentationPluginList = new ArrayList<>();
        Swagger3Properties.GlobalConfig globalConfig            = properties.getGlobalConfig();
        if (properties.getGroups().isEmpty()) {
            // 没有分组
            Docket docket = buildDocket("全部",
                    globalConfig.getPackagePath(),
                    globalConfig.getIncludePaths(),
                    globalConfig.getExcludePaths(),
                    buildApiInfo(null, false), convertRequestParameters(globalConfig.getApiRequestParameters()));
            documentationPluginList.add(docket);
        } else {
            properties.getGroups().keySet().forEach(key -> {
                Swagger3Properties.GroupConfig groupConfig = properties.getGroups().get(key);
                Docket docket = buildDocket(groupConfig.getTitle(),
                        groupConfig.getPackagePath(),
                        CollectionUtils.isEmpty(groupConfig.getIncludePaths()) ? properties.getGlobalConfig().getIncludePaths() : groupConfig.getIncludePaths(),
                        CollectionUtils.isEmpty(groupConfig.getExcludePaths()) ? properties.getGlobalConfig().getExcludePaths() : groupConfig.getExcludePaths(),
                        buildApiInfo(groupConfig, true),
                        convertRequestParameters(CollectionUtils.isEmpty(groupConfig.getApiRequestParameters()) ? properties.getGlobalConfig().getApiRequestParameters() : groupConfig.getApiRequestParameters()));
                documentationPluginList.add(docket);
            });
        }
        return OrderAwarePluginRegistry.of(documentationPluginList);
    }

    private List<RequestParameter> convertRequestParameters(List<Swagger3Properties.ApiRequestParameter> apiRequestParameters) {
        List<RequestParameter> requestParameters = new ArrayList<>();
        Optional.ofNullable(apiRequestParameters).ifPresent(input -> input.forEach(apiRequestParameter ->
                requestParameters.add(
                        new RequestParameterBuilder()
                                .name(apiRequestParameter.getName())
                                .in(apiRequestParameter.getIn())
                                .description(apiRequestParameter.getDescription())
                                .required(apiRequestParameter.getRequired()).build()
                ))
        );
        return requestParameters;
    }

    private Docket buildDocket(String groupName, String basePackage, List<String> include, List<String> exclude, ApiInfo apiInfo, List<RequestParameter> requestParameters) {
        Profiles profiles = Profiles.of(properties.getEnableEnv().split(","));
        return new Docket(DocumentationType.OAS_30)
                .groupName(groupName)
                .apiInfo(apiInfo)
                .select()
                .apis(RequestHandlerSelectors.basePackage(basePackage))
                .paths(PathSelectors.any())
                .build()
                .globalRequestParameters(requestParameters)
                .ignoredParameterTypes(HttpServletRequest.class, HttpServletResponse.class)
                .enable(environment.acceptsProfiles(profiles))
                .extensions(openApiExtensionResolver.buildExtensions(groupName));
    }

    private ApiInfo buildApiInfo(Swagger3Properties.GroupConfig groupConfig, boolean isGroup) {
        if (groupConfig != null && isGroup) {
            return new ApiInfoBuilder()
                    .title(StringUtils.hasLength(groupConfig.getTitle()) ? groupConfig.getTitle() : properties.getGlobalConfig().getTitle())
                    .description(StringUtils.hasLength(groupConfig.getDescription()) ? groupConfig.getDescription() : properties.getGlobalConfig().getDescription())
                    .version(StringUtils.hasLength(groupConfig.getVersion()) ? groupConfig.getVersion() : properties.getGlobalConfig().getVersion())
                    .license(StringUtils.hasLength(groupConfig.getLicense()) ? groupConfig.getLicense() : properties.getGlobalConfig().getLicense())
                    .licenseUrl(StringUtils.hasLength(groupConfig.getLicenseUrl()) ? groupConfig.getLicenseUrl() : properties.getGlobalConfig().getLicenseUrl())
                    .contact(Objects.isNull(groupConfig.getContact()) ? convertContact(groupConfig.getContact()) : convertContact(properties.getGlobalConfig().getContact()))
                    .build();
        }
        return new ApiInfoBuilder()
                .title(properties.getGlobalConfig().getTitle())
                .description(properties.getGlobalConfig().getDescription())
                .version(properties.getGlobalConfig().getVersion())
                .license(properties.getGlobalConfig().getLicense())
                .licenseUrl(properties.getGlobalConfig().getLicenseUrl())
                .contact(convertContact(properties.getGlobalConfig().getContact()))
                .build();
    }

    private Contact convertContact(Swagger3Properties.Contact contact) {
        return new Contact(contact.getContactName(), contact.getContactUrl(), contact.getContactEmail());
    }
}
