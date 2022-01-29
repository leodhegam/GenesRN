package com.pa2.genesrn.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactoryBean;

import java.util.Properties;

@Configuration
@PropertySource("classpath:env/mail.properties")
public class MailConfig {

    @Autowired
    private Environment env;

    @Bean
    public JavaMailSender javaMailService() {
        JavaMailSenderImpl javaMailSenderImpl = new JavaMailSenderImpl();
        javaMailSenderImpl.setHost(env.getProperty("mail.smtp.host"));
        javaMailSenderImpl.setPort(env.getProperty("mail.smtp.port", Integer.class));
        javaMailSenderImpl.setUsername(env.getProperty("mail.smtp.username"));
        javaMailSenderImpl.setPassword(env.getProperty("mail.smtp.password"));

        javaMailSenderImpl.setJavaMailProperties(javaMailProperties());

        return javaMailSenderImpl;
    }

    private Properties javaMailProperties() {

        Properties properties = new Properties();
        properties.setProperty("mail.transport.protocol", "smtp");
//        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.starttls.enable", "true");
        properties.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.setProperty("mail.debug", "true");

        return properties;
    }

    @Bean
    @Primary
    public FreeMarkerConfigurationFactoryBean factoryBean() {
        FreeMarkerConfigurationFactoryBean bean = new FreeMarkerConfigurationFactoryBean();
        bean.setTemplateLoaderPath("classpath:/templates");
        return bean;
    }
}


