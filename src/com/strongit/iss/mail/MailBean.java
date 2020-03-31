package com.strongit.iss.mail;

import java.io.File;
import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

public class MailBean implements Serializable {
	private static final long serialVersionUID = 3759976091813413863L;

	// 接收人邮箱
	private String[] toEmails;

	// 邮件主题
	private String subject;
	// 邮件内容
	private String text;
	// 邮件附件
	private File[] attachments;

	public MailBean(String toEmails, String subject, String text) {
		super();

		if (StringUtils.isNotBlank(toEmails)) {
			this.toEmails = toEmails.split(";");
		}
		this.subject = subject;
		this.text = text;
	}

	public MailBean(String[] toEmails, String subject, String text) {
		super();

		this.toEmails = toEmails;
		this.subject = subject;
		this.text = text;
	}

	public MailBean(String toEmails, String subject, String text,
			File[] attachments) {
		super();

		if (StringUtils.isNotBlank(toEmails)) {
			this.toEmails = toEmails.split(";");
		}
		this.subject = subject;
		this.text = text;
		this.attachments = attachments;
	}

	public MailBean(String[] toEmails, String subject, String text,
			File[] attachments) {
		super();

		this.toEmails = toEmails;
		this.subject = subject;
		this.text = text;
		this.attachments = attachments;
	}

	public String[] getToEmails() {
		return toEmails;
	}

	public void setToEmails(String[] toEmails) {
		this.toEmails = toEmails;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public File[] getAttachments() {
		return attachments;
	}

	public void setAttachments(File[] attachments) {
		this.attachments = attachments;
	}

}
