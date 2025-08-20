package ru.netology.config;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;

public class WebAppInitializer implements WebAppInitializer {
    @Override
    public void onStartup(ServletContext servletContext) {
        // Корневой контекст (сервисы, репозитории)
        var rootContext = new AnnotationConfigWebApplicationContext();
        rootContext.scan("ru.netology");
        servletContext.addListener(new ContextLoaderListener(rootContext));

        // Servlet контекст (контроллеры)
        var servletContextConfig = new AnnotationConfigWebApplicationContext();
        servletContextConfig.register(WebConfig.class);

        // DispatcherServlet
        var dispatcher = servletContext.addServlet("dispatcher", new DispatcherServlet(servletContextConfig));
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/api/*");
    }
}