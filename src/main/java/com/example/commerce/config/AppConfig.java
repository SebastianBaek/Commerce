package com.example.commerce.config;

import java.util.Properties;
import org.apache.commons.collections4.Trie;
import org.apache.commons.collections4.trie.PatriciaTrie;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AppConfig {

  @Value("${spring.mail.host}")
  private String host;

  @Value("${spring.mail.port}")
  private Integer port;

  @Value("${spring.mail.username}")
  private String username;

  @Value("${spring.mail.password}")
  private String password;

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public JavaMailSender javaMailService() {
    JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();

    javaMailSender.setHost(host);
    javaMailSender.setPort(port);
    javaMailSender.setUsername(username);
    javaMailSender.setPassword(password);

    javaMailSender.setJavaMailProperties(getMailProperties());

    return javaMailSender;
  }

  private Properties getMailProperties() {
    Properties properties = new Properties();
    properties.setProperty("mail.transport.protocol", "smtp");
    properties.setProperty("mail.smtp.auth", "true");
    properties.setProperty("mail.smtp.starttls.enable", "true");
    properties.setProperty("mail.debug", "true");
    properties.setProperty("mail.default.encoding", "UTF-8");
    return properties;
  }

  @Bean
  public Trie<String, String> trie() {
    return new PatriciaTrie<>();
  }
}
