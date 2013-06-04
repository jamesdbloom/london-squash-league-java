package org.squashleague.web.interceptor.bundling;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import ro.isdc.wro.http.support.ServletContextAttributeHelper;
import ro.isdc.wro.model.WroModel;

import javax.annotation.Resource;
import javax.servlet.ServletContext;

/**
 * @author jamesdbloom
 */
@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class WroModelHolder {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private ServletContext servletContext;
    private WroModel wroModel;

    public WroModel getWroModel() {
        if (wroModel == null) {
            try {
                ServletContextAttributeHelper helper = new ServletContextAttributeHelper(servletContext);
                if (helper.getManagerFactory() != null) {
                    wroModel = helper.getManagerFactory().create().getModelFactory().create();
                }
            } catch (Exception e) {
                logger.warn("Exception while creating WroModel", e);
            }
        }
        return wroModel;
    }

}
