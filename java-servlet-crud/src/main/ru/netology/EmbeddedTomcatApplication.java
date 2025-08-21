package ru.netology;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import java.io.File;

public class EmbeddedTomcatApplication {

    public static void main(String[] args) throws LifecycleException {
        // Порт и контекст
        int port = 8080;
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(port);
        tomcat.getConnector();

        // Временная директория
        File baseDir = new File(System.getProperty("java.io.tmpdir"));
        Context context = tomcat.addContext("", baseDir.getAbsolutePath());

        // Создаём Spring Context
        WebApplicationContext appContext = createContext();

        // Регистрируем DispatcherServlet
        DispatcherServlet servlet = new DispatcherServlet();
        servlet.setApplicationContext(appContext);

        Tomcat.addServlet(context, "dispatcher", servlet);
        context.addServletMappingDecoded("/api/*", "dispatcher");

        // Запускаем Tomcat
        tomcat.start();
        System.out.println("Server started at http://localhost:" + port);
        tomcat.getServer().await();
    }

    private static WebApplicationContext createContext() {
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.register(WebConfig.class);
        return context;
    }
}