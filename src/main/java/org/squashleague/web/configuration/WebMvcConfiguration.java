package org.squashleague.web.configuration;

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
import org.squashleague.dao.account.UserDAO;
import org.squashleague.dao.league.*;
import org.squashleague.domain.account.User;
import org.squashleague.domain.league.*;
import org.squashleague.web.interceptor.bundling.AddBundlingModelToViewModelInterceptor;
import org.squashleague.web.interceptor.bundling.WroModelHolder;
import ro.isdc.wro.manager.factory.ConfigurableWroManagerFactory;
import ro.isdc.wro.manager.factory.WroManagerFactory;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Properties;

/**
 * @author squashleague
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
    @Resource
    private UserDAO userDAO;

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(
                new Converter<String, Club>() {
                    public Club convert(String id) {
                        return clubDAO.findOne(Long.parseLong(id));
                    }
                });
        registry.addConverter(
                new Converter<String, League>() {
                    public League convert(String id) {
                        return leagueDAO.findOne(Long.parseLong(id));
                    }
                });
        registry.addConverter(
                new Converter<String, Division>() {
                    public Division convert(String id) {
                        return divisionDAO.findOne(Long.parseLong(id));
                    }
                });
        registry.addConverter(
                new Converter<String, Round>() {
                    public Round convert(String id) {
                        return roundDAO.findOne(Long.parseLong(id));
                    }
                });
        registry.addConverter(
                new Converter<String, Player>() {
                    public Player convert(String id) {
                        return playerDAO.findOne(Long.parseLong(id));
                    }
                });
        registry.addConverter(
                new Converter<String, Match>() {
                    public Match convert(String id) {
                        return matchDAO.findOne(Long.parseLong(id));
                    }
                });
        registry.addConverter(
                new Converter<String, User>() {
                    public User convert(String id) {
                        return userDAO.findOne(Long.parseLong(id));
                    }
                }
        );
    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AddBundlingModelToViewModelInterceptor(wroModelHolder, environment.getProperty("bundling.enabled")));
    }

    @Bean
    public FreeMarkerConfigurer freemarkerConfig() throws IOException, TemplateException {
        FreeMarkerConfigurer freeMarkerConfigurer = new FreeMarkerConfigurer();
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
