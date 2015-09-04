//
//  EmailHelper.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 4 Sep 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.helper;

import java.io.UnsupportedEncodingException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.willshex.blogwt.server.service.property.PropertyServiceProvider;
import com.willshex.blogwt.shared.api.datatype.Property;
import com.willshex.blogwt.shared.helper.PropertyHelper;

/**
 * @author William Shakour (billy1380)
 *
 */
public class EmailHelper {
	private static final Logger LOG = Logger.getLogger(EmailHelper.class
			.getName());

	public static boolean sendEmail (String to, String name, String subject,
			String body, boolean isHtml) {
		boolean sent = false;

		Property email = PropertyServiceProvider.provide().getNamedProperty(
				PropertyHelper.OUTGOING_EMAIL);

		if (!PropertyHelper.isEmpty(email)
				&& !PropertyHelper.NONE_VALUE.equals(email.value)) {
			Properties props = new Properties();
			Session session = Session.getDefaultInstance(props, null);

			try {
				Message msg = new MimeMessage(session);

				InternetAddress address = new InternetAddress(email.value,
						PropertyServiceProvider.provide().getNamedProperty(
								PropertyHelper.TITLE).value);

				msg.setFrom(address);
				msg.addRecipient(Message.RecipientType.TO, new InternetAddress(
						to, name));

				msg.setSubject(subject);

				if (isHtml) {
					msg.setContent(body, "text/html");
				} else {
					msg.setText(body);
				}

				Transport.send(msg);

				if (LOG.isLoggable(Level.INFO)) {
					LOG.info("Email sent successfully.");
				}

				sent = true;
			} catch (MessagingException | UnsupportedEncodingException e) {
				LOG.log(Level.WARNING,
						"Error sending email ["
								+ content(to, name, subject, body) + "]", e);
			}
		} else {
			if (LOG.isLoggable(Level.INFO)) {
				LOG.info("Property " + PropertyHelper.OUTGOING_EMAIL
						+ " not configured. Email ["
						+ content(to, name, subject, body) + "]");
			}
		}

		return sent;
	}

	private static String content (String to, String name, String subject,
			String body) {
		StringBuffer buffer = new StringBuffer();

		buffer.append("\nTo:\n");
		buffer.append(to);
		buffer.append("Name:\n");
		buffer.append(name);
		buffer.append("Subject:\n");
		buffer.append(subject);
		buffer.append("Body:\n");
		buffer.append(body);
		buffer.append("\n");

		return buffer.toString();
	}
}
