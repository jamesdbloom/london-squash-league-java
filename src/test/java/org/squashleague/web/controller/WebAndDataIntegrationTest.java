package org.squashleague.web.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.mock.web.MockFilterConfig;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import org.squashleague.configuration.RootConfiguration;
import org.squashleague.dao.league.HSQLApplicationContextInitializer;
import org.squashleague.service.email.EmailMockingConfiguration;
import org.squashleague.service.security.SpringSecurityUserContext;
import org.squashleague.web.configuration.WebMvcConfiguration;
import org.squashleague.web.controller.administration.MockDAOTest;

import javax.annotation.Resource;
import javax.servlet.ServletException;

import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * @author jamesdbloom
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextHierarchy({
        @ContextConfiguration(
                name = "root",
                classes = RootConfiguration.class,
                initializers = HSQLApplicationContextInitializer.class
        ),
        @ContextConfiguration(
                name = "dispatcher",
                classes = {WebMvcConfiguration.class, EmailMockingConfiguration.class},
                initializers = PropertyMockingApplicationContextInitializer.class
        )
})
public abstract class WebAndDataIntegrationTest extends MockDAOTest {

    @Resource
    protected WebApplicationContext webApplicationContext;
    protected MockMvc mockMvc;

    @Before
    public void setupFixture() throws ServletException {
        OpenEntityManagerInViewFilter openSessionInViewFilter = new OpenEntityManagerInViewFilter();
        openSessionInViewFilter.setPersistenceUnitName("persistenceUnit");
        openSessionInViewFilter.init(new MockFilterConfig(webApplicationContext.getServletContext(), "openSessionInViewFilter"));

        mockMvc = webAppContextSetup(webApplicationContext).addFilters(openSessionInViewFilter).build();
    }

}
