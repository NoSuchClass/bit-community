package com.bit.community.utils;

import java.util.Map;
import java.util.Properties;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import freemarker.template.Template;

@Service
public class MailSender implements InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(MailSender.class);
	private JavaMailSenderImpl mailSender;
	@Autowired
	FreeMarkerConfigurer freeMarkerConfigurer;
	
	public boolean sendWithHTMLTemplate(String to, String subject, Template template, Map<String, Object> model) {
		try {
			String nick = MimeUtility.encodeText("问答社区测试");
			InternetAddress from = new InternetAddress(nick + "<343788619@qq.com>");
			MimeMessage mimeMessage = mailSender.createMimeMessage();
			MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
			String result = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
			mimeMessageHelper.setTo(to);
			mimeMessageHelper.setFrom(from);
			mimeMessageHelper.setSubject(subject);
			mimeMessageHelper.setText(result, true);
			mailSender.send(mimeMessage);
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return false;
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		mailSender = new JavaMailSenderImpl();
		mailSender.setUsername("343788619@qq.com");
		mailSender.setPassword("ogocotheqbjubhcj");
		mailSender.setHost("smtp.qq.com");
		mailSender.setPort(465);
		mailSender.setProtocol("smtp");
		mailSender.setDefaultEncoding("utf8");
		Properties javaMailProperties = new Properties();
		javaMailProperties.put("mail.smtp.ssl.enable", true);
		mailSender.setJavaMailProperties(javaMailProperties);
	}

}
