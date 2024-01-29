package com.example.VirtualFridge;

import com.amazon.ask.servlet.ServletConstants;
import com.example.VirtualFridge.model.alexa.config.AlexaServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.http.HttpServlet;
import java.io.IOException;
import java.util.Properties;

@Configuration
public class WebConfig {

    public static final String SSL_KEYSTORE_FILE_PATH_KEY = "javax.net.ssl.keyStore";
    public static final String SSL_KEYSTORE_PASSWORD_KEY = "javax.net.ssl.keyStorePassword";

    @Bean
    public ServletRegistrationBean<HttpServlet> alexaServlet() {
        loadProperties();

        ServletRegistrationBean<HttpServlet> servRegBean = new ServletRegistrationBean<>();
        servRegBean.setServlet(new AlexaServlet());
        servRegBean.addUrlMappings("/api/v1.0/alexa");
        servRegBean.setLoadOnStartup(1);
        return servRegBean;
    }

    private void loadProperties() {
        System.setProperty(ServletConstants.TIMESTAMP_TOLERANCE_SYSTEM_PROPERTY, getPropertyValue(ServletConstants.TIMESTAMP_TOLERANCE_SYSTEM_PROPERTY));
        System.setProperty(ServletConstants.DISABLE_REQUEST_SIGNATURE_CHECK_SYSTEM_PROPERTY, getPropertyValue(ServletConstants.DISABLE_REQUEST_SIGNATURE_CHECK_SYSTEM_PROPERTY));
        System.setProperty(SSL_KEYSTORE_FILE_PATH_KEY, SSL_KEYSTORE_FILE_PATH_KEY);
        System.setProperty(SSL_KEYSTORE_PASSWORD_KEY, SSL_KEYSTORE_PASSWORD_KEY);
    }

    public static String getPropertyValue(String Key) {
        Properties prop = new Properties();

        try {
            prop.load(WebConfig.class.getResourceAsStream("/application.properties"));
            return prop.getProperty(Key);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }


}
