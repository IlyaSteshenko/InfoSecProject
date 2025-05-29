package com.informationsecurity.PasteBinService.configs;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.sql.DataSource;
import java.util.logging.Logger;

@Configuration
public class EmbeddedDatasource {
    public static boolean checkOnline(String url, String user, String password) {
        boolean answer;
        try (HikariDataSource dataSource = new HikariDataSource()) {
            dataSource.setJdbcUrl(url);
            dataSource.setUsername(user);
            dataSource.setPassword(password);
            answer = dataSource.getConnection().isValid(1000);
        } catch (Exception e) {
            Logger.getLogger("com.informationsecurity.PasteBinService").warning(e.getMessage());
            answer = false;
        }
        if(!answer) Logger.getLogger("com.informationsecurity.PasteBinService").warning("В данный момент база данных недоступна," +
                " приложение использует внутреннюю базу данных. ДАННЫЕ НЕ СОХРАНЯЮТСЯ");
        return answer;
    }

    // Основной DataSource (PostgreSQL)
    @Bean
    @Primary
    @ConditionalOnExpression("T(com.informationsecurity.PasteBinService.configs.EmbeddedDatasource).checkOnline('${spring.datasource.url}','${spring.datasource.username}','${spring.datasource.password}')")
    public DataSource primaryDataSource(Environment env) {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(env.getProperty("spring.datasource.url"));
        dataSource.setUsername(env.getProperty("spring.datasource.username"));
        dataSource.setPassword(env.getProperty("spring.datasource.password"));
        return dataSource;
    }

    @ConditionalOnMissingBean(DataSource.class)
    @Bean("h2DataSource")
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.h2.Driver");
        dataSource.setUrl("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;MODE=PostgreSQL");
        dataSource.setUsername("sa");
        dataSource.setPassword("");
        return dataSource;
    }

    @Bean
    @ConditionalOnBean(name = "h2DataSource")
    public DataSourceInitializer dataSourceInitializer(DataSource dataSource) {
        DataSourceInitializer initializer = new DataSourceInitializer();
        initializer.setDataSource(dataSource);

        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
//        populator.addScript(new ClassPathResource("/SQL/dbTestData.sql")); // Указываем скрипт

        initializer.setDatabasePopulator(populator);
        initializer.setEnabled(true); // Включить инициализацию

        return initializer;
    }
}
