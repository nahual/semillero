package org.nahual.semillero.config;

import org.springframework.web.context.AbstractContextLoaderInitializer;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.XmlWebApplicationContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

/**
 * Created by langosta on 1/30/14.
 */
public class SemilleroContextInitializer extends AbstractContextLoaderInitializer {

    @Override
    protected WebApplicationContext createRootApplicationContext() {
        return new XmlWebApplicationContext() {
            @Override
            protected String[] getDefaultConfigLocations() {
                return new String[] {"classpath:applicationContext.xml"};
            }
        };
    }

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        super.onStartup(servletContext);
    }
}
