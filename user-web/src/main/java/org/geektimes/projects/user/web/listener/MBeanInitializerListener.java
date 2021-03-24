package org.geektimes.projects.user.web.listener;

import org.geektimes.projects.user.domain.User;
import org.geektimes.projects.user.management.UserManager;

import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.lang.management.ManagementFactory;
import java.util.logging.Logger;

public class MBeanInitializerListener implements ServletContextListener {

    private final static Logger logger = Logger.getLogger(MBeanInitializerListener.class.getName());

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        logger.info("Initializing MBean");
        loadUserMBean();
    }

    private void loadUserMBean() {
        // 获取平台 MBean Server
        MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
        // 为 UserMXBean 定义 ObjectName
        ObjectName objectName = null;
        try {
            objectName = new ObjectName("jolokia:type=User");
        } catch (MalformedObjectNameException e) {
            e.printStackTrace();
        }
        // 创建 UserMBean 实例
        User user = new User();
        user.setId(1L);
        user.setName("MBeanExample");
        user.setPassword("******");

        try {
            mBeanServer.registerMBean(createUserMBean(user), objectName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Object createUserMBean(User user) {
        return new UserManager(user);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
