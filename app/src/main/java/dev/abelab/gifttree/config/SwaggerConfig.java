package dev.abelab.gifttree.config;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.beans.factory.annotation.Autowired;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import dev.abelab.gifttree.property.ProjectProperty;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Autowired
    ProjectProperty projectProperty;

    @Bean
    public Docket dock() {
        return new Docket(DocumentationType.SWAGGER_2) //
            .useDefaultResponseMessages(false) //
            .ignoredParameterTypes(ModelAttribute.class) //
            .protocols(Collections.singleton(this.projectProperty.getSwagger().getProtocol())) //
            .host(this.projectProperty.getSwagger().getHostname()) //
            .select() //
            .apis(RequestHandlerSelectors.basePackage("dev.abelab.gifttree.api.controller")) //
            .build() //
            .apiInfo(apiInfo()) //
            .tags( //
                new Tag("Auth", "認証"), //
                new Tag("Health Check", "ヘルスチェック"), //
                new Tag("User", "ユーザ"), //
                new Tag("Gift", "ギフト") //
            );
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder() //
            .title("Gift Tree Internal API") //
            .version(this.projectProperty.getVersion()) //
            .build();
    }
}
