package com.rrtyui.weatherappv2.util;

import com.rrtyui.weatherappv2.config.SpringConfig;
import com.rrtyui.weatherappv2.config.TestConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        SpringConfig.class,
        TestConfig.class
})
@WebAppConfiguration
public abstract class AbstractMvcTest {

    @Autowired
    protected WebApplicationContext wac;

    @Autowired
    protected ApplicationContext appContext;

    protected MockMvc mockMvc;

    @BeforeEach
    void setupMockMvc() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }
}