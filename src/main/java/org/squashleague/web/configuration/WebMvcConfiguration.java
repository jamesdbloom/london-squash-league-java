package org.squashleague.web.configuration;

import freemarker.template.DefaultObjectWrapper;
import freemarker.template.TemplateException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.env.Environment;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;
import org.squashleague.dao.account.RoleDAO;
import org.squashleague.dao.account.UserDAO;
import org.squashleague.dao.league.*;
import org.squashleague.domain.account.Role;
import org.squashleague.domain.account.User;
import org.squashleague.domain.league.*;
import org.squashleague.web.interceptor.bundling.AddBundlingModelToViewModelInterceptor;
import org.squashleague.web.interceptor.bundling.WroModelHolder;
import org.squashleague.web.interceptor.navigation.NavigationInterceptor;
import ro.isdc.wro.manager.factory.ConfigurableWroManagerFactory;
import ro.isdc.wro.manager.factory.WroManagerFactory;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author jamesdbloom
 */
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"org.squashleague.web"})
public class WebMvcConfiguration extends WebMvcConfigurerAdapter {

    @Resource
    private Environment environment;
    @Resource
    private WroModelHolder wroModelHolder;
    @Resource
    private RoleDAO roleDAO;
    @Resource
    private UserDAO userDAO;
    @Resource
    private ClubDAO clubDAO;
    @Resource
    private LeagueDAO leagueDAO;
    @Resource
    private DivisionDAO divisionDAO;
    @Resource
    private RoundDAO roundDAO;
    @Resource
    private PlayerDAO playerDAO;
    @Resource
    private MatchDAO matchDAO;

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(
                new Converter<String[], List<Role>>() {
                    public List<Role> convert(String[] names) {
                        List<Role> roles = new ArrayList<>();
                        for (String name : names) {
                            roles.add(roleDAO.findByName(name));
                        }
                        return roles;
                    }
                });
        registry.addConverter(
                new Converter<String, Role>() {
                    public Role convert(String name) {
                        return roleDAO.findByName(name);
                    }
                });
        registry.addConverter(
                new Converter<String, User>() {
                    public User convert(String id) {
                        try {
                            return userDAO.findById(Long.parseLong(id));
                        } catch (NumberFormatException nfe) {
                            return null;
                        }
                    }
                });
        registry.addConverter(
                new Converter<String, Club>() {
                    public Club convert(String id) {
                        try {
                            return clubDAO.findById(Long.parseLong(id));
                        } catch (NumberFormatException nfe) {
                            return null;
                        }
                    }
                });
        registry.addConverter(
                new Converter<String, League>() {
                    public League convert(String id) {
                        try {
                            return leagueDAO.findById(Long.parseLong(id));
                        } catch (NumberFormatException nfe) {
                            return null;
                        }
                    }
                });
        registry.addConverter(
                new Converter<String, Division>() {
                    public Division convert(String id) {
                        try {
                            return divisionDAO.findById(Long.parseLong(id));
                        } catch (NumberFormatException nfe) {
                            return null;
                        }
                    }
                });
        registry.addConverter(
                new Converter<String, Round>() {
                    public Round convert(String id) {
                        try {
                            return roundDAO.findById(Long.parseLong(id));
                        } catch (NumberFormatException nfe) {
                            return null;
                        }
                    }
                });
        registry.addConverter(
                new Converter<String, Player>() {
                    public Player convert(String id) {
                        try {
                            return playerDAO.findById(Long.parseLong(id));
                        } catch (NumberFormatException nfe) {
                            return null;
                        }
                    }
                });
        registry.addConverter(
                new Converter<String, Match>() {
                    public Match convert(String id) {
                        try {
                            return matchDAO.findById(Long.parseLong(id));
                        } catch (NumberFormatException nfe) {
                            return null;
                        }
                    }
                });
    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AddBundlingModelToViewModelInterceptor(wroModelHolder, environment.getProperty("bundling.enabled")));
        registry.addInterceptor(new NavigationInterceptor());
    }

    @Bean
    public FreeMarkerConfigurer freemarkerConfig() throws IOException, TemplateException {
        FreeMarkerConfigurer freeMarkerConfigurer = new FreeMarkerConfigurer() {
            protected void postProcessConfiguration(freemarker.template.Configuration config) throws IOException, TemplateException {
                super.postProcessConfiguration(config);
                ((DefaultObjectWrapper) config.getObjectWrapper()).setExposeFields(true);
            }
        };
        freeMarkerConfigurer.setTemplateLoaderPath("/");
        freeMarkerConfigurer.setFreemarkerSettings(new Properties() {{
            setProperty("template_exception_handler", "DEBUG");
            setProperty("strict_syntax", "true");
            setProperty("whitespace_stripping", "true");
        }});
        return freeMarkerConfigurer;
    }

    @Bean
    public FreeMarkerViewResolver freeMarkerViewResolver() {
        FreeMarkerViewResolver freeMarkerViewResolver = new FreeMarkerViewResolver();
        freeMarkerViewResolver.setOrder(1);
        freeMarkerViewResolver.setPrefix("/WEB-INF/view/");
        freeMarkerViewResolver.setSuffix(".ftl");
        freeMarkerViewResolver.setContentType("text/html;charset=UTF-8");
        return freeMarkerViewResolver;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
    }

    @Bean
    public WroManagerFactory wroManagerFactory() {
        ConfigurableWroManagerFactory wroManagerFactory = new ConfigurableWroManagerFactory();

        wroManagerFactory.setConfigProperties(new Properties() {{
            setProperty("debug", environment.getProperty("bundling.enabled"));
            setProperty("preProcessors", "cssImport,semicolonAppender,conformColors");
            setProperty("postProcessors", "yuiCssMin,googleClosureAdvanced");
            setProperty("cacheGzippedContent", "true");
            setProperty("hashStrategy", "MD5"); // should drive the naming strategy to fingerprint resource urls - NOT YET WORKING / CONFIGURED CORRECTLY
            setProperty("namingStrategy", "hashEncoder-CRC32"); // should drive the naming strategy to fingerprint resource urls - NOT YET WORKING / CONFIGURED CORRECTLY
        }});

        return wroManagerFactory;
    }
}
