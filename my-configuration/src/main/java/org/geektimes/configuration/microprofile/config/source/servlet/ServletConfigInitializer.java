package org.geektimes.configuration.microprofile.config.source.servlet;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.util.Set;

public class ServletConfigInitializer implements ServletContainerInitializer {

    @Override
    public void onStartup(Set<Class<?>> c, ServletContext servletContext) {
        // 增加 ServletContextListener
        System.out.println("ServletConfigInitializer running");
        servletContext.addListener(ServletContextConfigInitializer.class);
    }
}
