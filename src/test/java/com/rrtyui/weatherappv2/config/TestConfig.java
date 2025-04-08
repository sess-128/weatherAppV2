package com.rrtyui.weatherappv2.config;

import com.rrtyui.weatherappv2.interceptor.SessionManager;
import com.rrtyui.weatherappv2.service.CookieService;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import org.hibernate.SessionFactory;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.mock.web.MockServletContext;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

import javax.sql.DataSource;
import java.util.Properties;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Configuration
@Profile("test")
public class TestConfig {

    @Bean
    @Primary
    public SessionManager sessionManager() throws Exception {
        SessionManager mock = mock(SessionManager.class);
        when(mock.preHandle(any(), any(), any())).thenReturn(true);
        return mock;
    }

    @Bean
    @Primary
    public RestTemplate restTemplate() {
        return mock(RestTemplate.class);
    }

    @Bean
    @Primary
    public CookieService cookieService() {
        return mock(CookieService.class);
    }

    @Bean
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .setName("test_db")
                .build();
    }

    @Bean
    public LocalSessionFactoryBean sessionFactory(DataSource dataSource) {
        LocalSessionFactoryBean factoryBean = new LocalSessionFactoryBean();
        factoryBean.setDataSource(dataSource);
        factoryBean.setPackagesToScan("com.rrtyui.weatherappv2.entity");
        factoryBean.setHibernateProperties(hibernateProperties());
        return factoryBean;
    }

    private Properties hibernateProperties() {
        Properties props = new Properties();
        props.put("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        props.put("hibernate.hbm2ddl.auto", "create-drop"); // создаёт схему перед каждым тестом
        props.put("hibernate.show_sql", true);
        return props;
    }

    @Bean
    public HibernateTransactionManager transactionManager(SessionFactory sessionFactory) {
        return new HibernateTransactionManager(sessionFactory);
    }

//    @Bean
//    @Primary
//    public ITemplateResolver templateResolver() {
//        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
//        resolver.setPrefix("templates/");
//        resolver.setSuffix(".html");
//        return resolver;
//    }
//
//    @Bean
//    @Primary
//    public SpringTemplateEngine templateEngine() {
//        return Mockito.mock(SpringTemplateEngine.class);
//    }

    @Bean
    @Primary
    public HttpServletRequest httpServletRequest() {
        HttpServletRequest mockRequest = Mockito.mock(HttpServletRequest.class);
        Mockito.when(mockRequest.getCookies()).thenReturn(null);
        return mockRequest;
    }

    @Bean
    @Primary
    public ServletContext servletContext() {
        return new MockServletContext();
    }
    @Bean
    @Primary
    public ViewResolver viewResolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/WEB-INF/views/");
        resolver.setSuffix(".html");
        return resolver;
    }
}