package com.xxl.job.admin.core.util;

import com.xxl.job.admin.config.XxlJobAdminConfig;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.io.File;
import java.util.Properties;

/**
 * 邮件发送.Util
 *
 * @author xuxueli 2016-3-12 15:06:20
 */
public class MailUtil {
	private static Logger logger = LoggerFactory.getLogger(MailUtil.class);
	
	/**
	 *
	 * @param toAddress		收件人邮箱
	 * @param mailSubject	邮件主题
	 * @param mailBody		邮件正文
	 * @return
	 */
	public static boolean sendMail(String toAddress, String mailSubject, String mailBody){

		try {
			// Create the email message
			HtmlEmail email = new HtmlEmail();

			//email.setDebug(true);		// 将会打印一些log
			//email.setTLS(true);		// 是否TLS校验，，某些邮箱需要TLS安全校验，同理有SSL校验
			//email.setSSL(true);

			email.setHostName(XxlJobAdminConfig.getAdminConfig().getMailHost());

			if (XxlJobAdminConfig.getAdminConfig().isMailSSL()) {
				email.setSslSmtpPort(XxlJobAdminConfig.getAdminConfig().getMailPort());
				email.setSSLOnConnect(true);
			} else {
				email.setSmtpPort(Integer.valueOf(XxlJobAdminConfig.getAdminConfig().getMailPort()));
			}

			email.setAuthenticator(new DefaultAuthenticator(XxlJobAdminConfig.getAdminConfig().getMailUsername(), XxlJobAdminConfig.getAdminConfig().getMailPassword()));
			email.setCharset("UTF-8");

			email.setFrom(XxlJobAdminConfig.getAdminConfig().getMailUsername(), XxlJobAdminConfig.getAdminConfig().getMailSendNick());
			email.addTo(toAddress);
			email.setSubject(mailSubject);
			email.setMsg(mailBody);

			//email.attach(attachment);	// add the attachment

			email.send();				// send the email
			return true;
		} catch (EmailException e) {
			logger.error(e.getMessage(), e);

		}
		return false;
	}
	/**
	 * 发送邮件 (完整版) (纯JavaMail)
	 *
	 * @param toAddress		: 收件人邮箱
	 * @param mailSubject	: 邮件主题
	 * @param mailBody		: 邮件正文
	 * @param mailBodyIsHtml: 邮件正文格式,true:HTML格式;false:文本格式
	 * //@param inLineFile	: 内嵌文件
	 * @param attachments	: 附件
	 */
	public static boolean sendMail (String toAddress, String mailSubject, String mailBody,
									boolean mailBodyIsHtml, File[] attachments){
		try {
			// 创建邮件发送类 JavaMailSender (用于发送多元化邮件，包括附件，图片，html 等    )
			JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
			mailSender.setHost(XxlJobAdminConfig.getAdminConfig().getMailHost()); 			// 设置邮件服务主机
			mailSender.setUsername(XxlJobAdminConfig.getAdminConfig().getMailUsername()); 	// 发送者邮箱的用户名
			mailSender.setPassword(XxlJobAdminConfig.getAdminConfig().getMailPassword()); 	// 发送者邮箱的密码

			//配置文件，用于实例化java.mail.session
			Properties pro = new Properties();
			pro.put("mail.smtp.auth", "true");		// 登录SMTP服务器,需要获得授权 (网易163邮箱新近注册的邮箱均不能授权,测试 sohu 的邮箱可以获得授权)
			pro.put("mail.smtp.socketFactory.port", XxlJobAdminConfig.getAdminConfig().getMailPort());
			pro.put("mail.smtp.socketFactory.fallback", "false");
			mailSender.setJavaMailProperties(pro);

			//创建多元化邮件 (创建 mimeMessage 帮助类，用于封装信息至 mimeMessage)
			MimeMessage mimeMessage = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, ArrayUtils.isNotEmpty(attachments), "UTF-8");

			helper.setFrom(XxlJobAdminConfig.getAdminConfig().getMailUsername(), "XXL_JOB");
			helper.setTo(toAddress);

			helper.setSubject(mailSubject);
			helper.setText(mailBody, mailBodyIsHtml);

			// 添加内嵌文件，第1个参数为cid标识这个文件,第2个参数为资源
			//helper.addInline(MimeUtility.encodeText(inLineFile.getName()), inLineFile);

			// 添加附件
			if (ArrayUtils.isNotEmpty(attachments)) {
				for (File file : attachments) {
					helper.addAttachment(MimeUtility.encodeText(file.getName()), file);
				}
			}

			mailSender.send(mimeMessage);
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return false;
	}

}
