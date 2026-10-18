package co.edu.unicauca.microservices.pkgQuestionService;

import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
    basePackages = "co.edu.unicauca.microservices.pkgQuestionService",
    entityManagerFactoryRef = "questionEntityManagerFactory",
    transactionManagerRef = "questionTransactionManager"
)
public class clsQuestionDbConfig {

    @Value("${spring.datasource.secondary.url}")
    private String url;

    @Value("${spring.datasource.secondary.username}")
    private String username;

    @Value("${spring.datasource.secondary.password}")
    private String password;

    @Value("${spring.datasource.secondary.driver-class-name}")
    private String driverClassName;

    @Bean(name = "questionDataSource")
    public DataSource questionDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setDriverClassName(driverClassName);
        return dataSource;
    }

    @Bean(name = "questionEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean questionEntityManagerFactory(
            @Qualifier("questionDataSource") DataSource dataSource) {

        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setDataSource(dataSource);
        factory.setPackagesToScan("co.edu.unicauca.microservices.pkgQuestionService");
        factory.setPersistenceUnitName("db_questions");

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        factory.setJpaVendorAdapter(vendorAdapter);

        Map<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", "update");
        properties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        factory.setJpaPropertyMap(properties);

        return factory;
    }

    @Bean(name = "questionTransactionManager")
    public PlatformTransactionManager questionTransactionManager(
            @Qualifier("questionEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}