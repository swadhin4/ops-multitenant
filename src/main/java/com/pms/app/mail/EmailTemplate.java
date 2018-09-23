/*
 * Copyright (C) 2013 , Inc. All rights reserved 
 */
package com.pms.app.mail;

import java.util.List;

public class EmailTemplate {

	/** The to. */
	private List<String> to;

	/** The cc. */
	private List<String> cc;

	/** The bcc. */
	private List<String> bcc;

	/** The text. */
	private String text;

	/** The subject. */
	private String subject;

	/** The attachment file name. */
	private String attachmentFileName;

	/**
	 * Instantiates a new email template.
	 */
	public EmailTemplate() {
	}

	/**
	 * Instantiates a new email template.
	 * 
	 * @param to
	 *            the to
	 * @param text
	 *            the text
	 * @param subject
	 *            the subject
	 */
	public EmailTemplate(final List<String> to, final String text, final String subject) {
		super();
		this.to = to;
		this.text = text;
		this.subject = subject;
	}

	/**
	 * Instantiates a new email template.
	 * 
	 * @param to
	 *            the to
	 * @param cc
	 *            the cc
	 * @param bcc
	 *            the bcc
	 * @param text
	 *            the text
	 * @param subject
	 *            the subject
	 * @param attachmentFileName
	 *            the attachment file name
	 */
	public EmailTemplate(final List<String> to, final List<String> cc, final List<String> bcc,
			final String text, final String subject, final String attachmentFileName) {
		super();
		this.to = to;
		this.cc = cc;
		this.bcc = bcc;
		this.text = text;
		this.subject = subject;
		this.attachmentFileName = attachmentFileName;
	}

	/**
	 * Instantiates a new email template.
	 * 
	 * @param to
	 *            the to
	 * @param text
	 *            the text
	 * @param subject
	 *            the subject
	 * @param attachmentFileName
	 *            the attachment file name
	 */
	public EmailTemplate(final List<String> to, final String text, final String subject,
			final String attachmentFileName) {
		super();
		this.to = to;
		this.text = text;
		this.subject = subject;
		this.attachmentFileName = attachmentFileName;
	}

	/**
	 * Gets the to.
	 * 
	 * @return the to
	 */
	public List<String> getTo() {
		return to;
	}

	/**
	 * Sets the to.
	 * 
	 * @param to
	 *            the new to
	 */
	public void setTo(final List<String> to) {
		this.to = to;
	}

	/**
	 * Gets the cc.
	 * 
	 * @return the cc
	 */
	public List<String> getCc() {
		return cc;
	}

	/**
	 * Sets the cc.
	 * 
	 * @param cc
	 *            the new cc
	 */
	public void setCc(final List<String> cc) {
		this.cc = cc;
	}

	/**
	 * Gets the bcc.
	 * 
	 * @return the bcc
	 */
	public List<String> getBcc() {
		return bcc;
	}

	/**
	 * Sets the bcc.
	 * 
	 * @param bcc
	 *            the new bcc
	 */
	public void setBcc(final List<String> bcc) {
		this.bcc = bcc;
	}

	/**
	 * Gets the attachment file name.
	 * 
	 * @return the attachment file name
	 */
	public String getAttachmentFileName() {
		return attachmentFileName;
	}

	/**
	 * Sets the attachment file name.
	 * 
	 * @param attachmentFileName
	 *            the new attachment file name
	 */
	public void setAttachmentFileName(final String attachmentFileName) {
		this.attachmentFileName = attachmentFileName;
	}

	/**
	 * Gets the text.
	 * 
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * Sets the text.
	 * 
	 * @param text
	 *            the new text
	 */
	public void setText(final String text) {
		this.text = text;
	}

	/**
	 * Gets the subject.
	 * 
	 * @return the subject
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * Sets the subject.
	 * 
	 * @param subject
	 *            the new subject
	 */
	public void setSubject(final String subject) {
		this.subject = subject;
	}

}