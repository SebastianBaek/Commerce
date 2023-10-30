package com.example.commerce.common;

import java.io.UnsupportedEncodingException;
import javax.mail.internet.InternetAddress;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

@Slf4j
@Component
@RequiredArgsConstructor
public class MailComponent {

  private static final String fromName = "관리자";
  private final JavaMailSender javaMailSender;
  private final SpringTemplateEngine templateEngine;
  @Value("${spring.mail.username}")
  private String fromEmail;

  public void registerVerifyEmailSend(Long id, String toEmail, String toName) {
    String title = "[Commerce] 회원가입을 위해 메일을 인증해 주세요.";
    String verification_url = "http://localhost:8080/user/verify/" + id;

    Context context = new Context();
    context.setVariable("username", toName);
    context.setVariable("verification_url", verification_url);

    String contents = templateEngine.process("register", context);

    MimeMessagePreparator mimeMessagePreparator = mimeMessage -> {
      MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

      InternetAddress from = addressFromTo(fromEmail, fromName);

      InternetAddress to = addressFromTo(toEmail, toName);

      mimeMessageHelper.setFrom(from);
      mimeMessageHelper.setTo(to);
      mimeMessageHelper.setSubject(title);
      mimeMessageHelper.setText(contents, true);
    };
    try {
      javaMailSender.send(mimeMessagePreparator);
    } catch (Exception e) {
      log.info(e.getMessage());
    }
  }

  private InternetAddress addressFromTo(String email, String name) {
    InternetAddress address = new InternetAddress();
    try {
      address.setAddress(email);
      address.setPersonal(name);
    } catch (UnsupportedEncodingException e) {
      log.info(e.getMessage());
    }
    return address;
  }
}
