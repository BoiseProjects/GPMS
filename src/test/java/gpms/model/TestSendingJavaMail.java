package gpms.model;

import gpms.utils.EmailUtil;

import java.io.IOException;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;

public class TestSendingJavaMail {

	public static void main(String[] args) throws Exception {

		// Java Program to send Email using SMTP without authentication
		System.out.println("SimpleEmail Start");

		String smtpHostServer = "smtp.seal.boisestate.edu";
		String emailID = "milsonmun@gmail.com";

		Properties props = System.getProperties();

		props.put("mail.smtp.host", smtpHostServer);

		Session session = Session.getInstance(props, null);

		EmailUtil emailUtil = new EmailUtil();

		emailUtil.sendEmail(session, emailID, "SimpleEmail Testing Subject",
				"SimpleEmail Testing Body");

		// Java Program to Send Email with TLS Authentication
		/**
		 * Outgoing Mail (SMTP) Server requires TLS or SSL: smtp.gmail.com (use
		 * authentication) Use Authentication: Yes Port for TLS/STARTTLS: 587
		 */
		final String fromEmail = "noreplygpms@gmail.com"; // requires valid
															// gmail
															// id
		final String password = "****"; // correct password for gmail id
		final String toEmail = "milsonmun@yahoo.com"; // can be any email id

		// /////////////////////////////////////////////////////////////
		// IMPORTANT NOTE //////////////////////////////////////////////
		// /YOU MUST ENABLE LESS SECURE APPS FOR THIS TO WORK///////////
		// https://www.google.com/settings/u/1/security/lesssecureapps//
		// /////////////////////////////////////////////////////////////

		System.out.println("TLSEmail Start");
		props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com"); // SMTP Host
		props.put("mail.smtp.port", "587"); // TLS Port
		props.put("mail.smtp.auth", "true"); // enable authentication
		props.put("mail.smtp.starttls.enable", "true"); // enable STARTTLS

		// props1.put("mail.smtp.starttls.required", "true");

		// create Authenticator object to pass in Session.getInstance argument
		Authenticator auth = new Authenticator() {
			// override the getPasswordAuthentication method
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(fromEmail, password);
			}
		};
		session = Session.getInstance(props, auth);

		emailUtil.sendEmail(session, toEmail, "TLSEmail Testing Subject",
				"TLSEmail Testing Body");

		// Java Program to send Email with SSL Authentication
		/**
		 * Outgoing Mail (SMTP) Server requires TLS or SSL: smtp.gmail.com (use
		 * authentication) Use Authentication: Yes Port for SSL: 465
		 */

		System.out.println("SSLEmail Start");
		props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com"); // SMTP Host
		props.put("mail.smtp.socketFactory.port", "465"); // SSL Port
		props.put("mail.smtp.socketFactory.class",
				"javax.net.ssl.SSLSocketFactory"); // SSL Factory Class
		// props.put("mail.smtp.socketFactory.fallback", "true");

		props.put("mail.smtp.auth", "true"); // Enabling SMTP Authentication
		props.put("mail.smtp.port", "465"); // SMTP Port

		auth = new Authenticator() {
			// override the getPasswordAuthentication method
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(fromEmail, password);
			}
		};

		session = Session.getDefaultInstance(props, auth);
		System.out.println("Session created");

		emailUtil.sendEmail(session, toEmail, "SSLEmail Testing Subject",
				"<h1>SSLEmail Testing Body</h1>");

		// Test for Attachment with email
		emailUtil = new EmailUtil("/XACMLDatasheet.xls");
		emailUtil.sendAttachmentEmail(session, toEmail,
				"SSLEmail Testing Subject with Attachment",
				"<h1>SSLEmail Testing Body with Attachment</h1>",
				"rulemapping.xls");

		// Test for image embedded with email
		emailUtil = new EmailUtil("/teapot.jpg");
		emailUtil.sendImageEmail(session, toEmail,
				"SSLEmail Testing Subject with Image",
				"<h1>SSLEmail Testing Body with Image</h1>", "teapot.jpg");

	}
}
