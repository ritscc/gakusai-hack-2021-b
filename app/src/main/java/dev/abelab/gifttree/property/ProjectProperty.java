package dev.abelab.gifttree.property;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.*;

@Data
@Configuration
@ConfigurationProperties("project")
public class ProjectProperty {

    /**
     * プロジェクト名
     */
    String name;

    /**
     * バージョン
     */
    String version;

    /**
     * Swagger
     */
    Swagger swagger;

    @Data
    public static class Swagger {

        /**
         * プロトコル
         */
        String protocol;

        /**
         * ホスト名
         */
        String hostname;

    }

}
