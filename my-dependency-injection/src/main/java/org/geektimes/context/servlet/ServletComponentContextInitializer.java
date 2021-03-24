package org.geektimes.context.servlet;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.util.Set;

public class ServletComponentContextInitializer implements ServletContainerInitializer {

    @Override
    public void onStartup(Set<Class<?>> c, ServletContext servletContext) throws ServletException {
        // 增加 ServletContextListener
        System.out.println("Initializing component context");
        servletContext.addListener(ComponentContextInitializerListener.class);
    }
}
