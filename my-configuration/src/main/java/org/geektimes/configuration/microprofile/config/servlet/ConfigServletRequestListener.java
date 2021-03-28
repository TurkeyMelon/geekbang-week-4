package org.geektimes.configuration.microprofile.config.servlet;

import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.spi.ConfigProviderResolver;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;

public class ConfigServletRequestListener implements ServletRequestListener {

    private static final ThreadLocal<Config> CONFIG_THREAD_LOCAL = new ThreadLocal<>();

    @Override
    public void requestInitialized(ServletRequestEvent sre) {
        ServletRequest request = sre.getServletRequest();
        ServletContext context = request.getServletContext();
        ClassLoader classLoader = context.getClassLoader();
        ConfigProviderResolver instance = ConfigProviderResolver.instance();
        Config config = instance.getConfig(classLoader);
        CONFIG_THREAD_LOCAL.set(config);
    }

    @Override
    public void requestDestroyed(ServletRequestEvent sre) {
        CONFIG_THREAD_LOCAL.remove();
    }

    public static Config getConfig() {
        return CONFIG_THREAD_LOCAL.get();
    }
}
