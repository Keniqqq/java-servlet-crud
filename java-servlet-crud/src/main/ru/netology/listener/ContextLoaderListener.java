package ru.netology.listener;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.netology.config.AppConfig;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class ContextLoaderListener implements ServletContextListener {

    private AnnotationConfigApplicationContext context;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        context = new AnnotationConfigApplicationContext(AppConfig.class);
        sce.getServletContext().setAttribute("applicationContext", context);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if (context != null) {
            context.close();
        }
    }
}