package dev.abelab.gifttree.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@MapperScan("dev.abelab.gifttree.db.mapper")
@Configuration
public class MyBatisConfig {
}
