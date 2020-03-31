package com.strongit.iss.mail;

import java.io.File;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import com.strongit.iss.exception.BusinessServiceException;

/**
 * 邮件发送共通
 * 
 * @author jian
 *
 */
public class ISSMail {
	Logger log = Logger.getLogger(ISSMail.class);

	private JavaMailSender mailSender;
	private SimpleMailMessage mailMessage;

	/**
	 * 发送邮件
	 * 
	 * @param mailBean
	 *            MailBean 邮件实体
	 * @return
	 * @throws BusinessServiceException
	 */
	public boolean send(MailBean mailBean) throws BusinessServiceException {
		
		try {
			// 邮件内容校验
			this.checkMail(mailBean);
			
			// 创建MimeMessage
			MimeMessage mimeMessage = createMimeMessage(mailBean);

			// 邮件发送
			getMailSender().send(mimeMessage);
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessServiceException(e.getMessage(), e);
		}
		
		return true;
	}

	/**
	 * 创建MimeMessage
	 * 
	 * @param mailBean
	 *            MailBean 邮件实体
	 * @return
	 * @throws BusinessServiceException
	 */
	private MimeMessage createMimeMessage(MailBean mailBean)
			throws BusinessServiceException {
		MimeMessage mimeMessage = null;

		try {
			mimeMessage = getMailSender().createMimeMessage();
			MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
			// 发件人邮箱
			messageHelper.setFrom(getMailMessage().getFrom());
			// 收件人邮箱
			messageHelper.setTo(mailBean.getToEmails());
			// 邮件主题
			messageHelper.setSubject(mailBean.getSubject());
			// 邮件内容
			messageHelper.setText(mailBean.getText(),true);

			// 邮件附件
			if (mailBean.getAttachments() != null && mailBean.getAttachments().length > 0) {
				for (File attachment : mailBean.getAttachments()) {
					// 添加附件
					if (attachment != null) {
						messageHelper.addAttachment(attachment.getName(), attachment);
					}
				}
			}
		} catch (MessagingException e) {
			throw new BusinessServiceException(e.getMessage(), e);
		}

		return mimeMessage;
	}

	/**
	 * 邮件校验
	 * 
	 * @param mailBean
	 *            MailBean 邮件实体
	 * @return
	 * @throws BusinessServiceException
	 */
	private boolean checkMail(MailBean mailBean)
			throws BusinessServiceException {
		if (mailBean == null) {
			throw new BusinessServiceException("邮件对象为空.");
		}

		if (StringUtils.isBlank(getMailMessage().getFrom())) {
			throw new BusinessServiceException("发件人邮箱为空.");
		}

		if (mailBean.getToEmails() == null
				|| mailBean.getToEmails().length <= 0) {
			throw new BusinessServiceException("收件人邮箱为空.");
		}

		if (StringUtils.isBlank(mailBean.getSubject())) {
			throw new BusinessServiceException("邮件主题为空.");
		}

		if (StringUtils.isBlank(mailBean.getText())) {
			throw new BusinessServiceException("邮件内容为空.");
		}

		return true;
	}

	public JavaMailSender getMailSender() {
		return mailSender;
	}

	public void setMailSender(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

	public SimpleMailMessage getMailMessage() {
		return mailMessage;
	}

	public void setMailMessage(SimpleMailMessage mailMessage) {
		this.mailMessage = mailMessage;
	}

}