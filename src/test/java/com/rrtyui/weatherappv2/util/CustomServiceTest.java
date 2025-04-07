package com.rrtyui.weatherappv2.util;

import com.rrtyui.weatherappv2.config.HibernateConfig;
import com.rrtyui.weatherappv2.config.MockServletContextInitializer;
import com.rrtyui.weatherappv2.config.SpringConfig;
import com.rrtyui.weatherappv2.config.TestConfig;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(
        classes = {SpringConfig.class, HibernateConfig.class, TestConfig.class},
        initializers = MockServletContextInitializer.class
)
@Transactional
public @interface CustomServiceTest {
}
