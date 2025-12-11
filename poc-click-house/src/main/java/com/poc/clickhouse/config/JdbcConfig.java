package com.poc.clickhouse.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jdbc.repository.config.AbstractJdbcConfiguration;
import org.springframework.data.relational.core.dialect.Dialect;
import org.springframework.data.relational.core.dialect.H2Dialect;

@Configuration
public class JdbcConfig extends AbstractJdbcConfiguration {
    
    @Bean
    public Dialect jdbcDialect() {
        return H2Dialect.INSTANCE; // 根据实际数据库类型选择对应方言
    }
}
