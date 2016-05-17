package gpms.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class EmailUtil {

	private static String filePath = new String();

	// Assuming you are sending email from localhost
	final String smtpHostServer = "localhost";

	// Get system properties
	Properties properties = System.getProperties();

	public static String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		EmailUtil.filePath = filePath;
	}

	public EmailUtil() {
		// Setup mail server
		properties.setProperty("mail.smtp.host", smtpHostServer);
	}

	public EmailUtil(String attachmentFile) throws Exception {
		// Setup mail server
		properties.setProperty("mail.smtp.host", smtpHostServer);
		this.setFilePath(this.getClass()
				.getResource("/uploads" + attachmentFile).toURI().getPath());
	}

	// Java Program to send Email using SMTP without authentication
	public void sendMailWithoutAuth(String emailID, String subject, String body) {
		Session session = Session.getInstance(properties, null);
		sendEmail(session, emailID, subject, body);
	}

	public void sendMailMultipleUsersWithoutAuth(String piEmail,
			List<String> emaillist, String subject, String body) {
		Session session = Session.getInstance(properties, null);
		sendEmailToMultipleUsers(session, piEmail, emaillist, subject, body);
	}

	private void sendEmailToMultipleUsers(Session session, String toEmail,
			List<String> emaillist, String subject, String body) {
		try {
			// Create a default MimeMessage object.
			MimeMessage msg = new MimeMessage(session);

			// set message headers
			msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
			msg.addHeader("format", "flowed");
			msg.addHeader("Content-Transfer-Encoding", "8bit");

			msg.setFrom(new InternetAddress("do-not-reply@seal.boisestate.edu",
					"do-not-reply@seal.boisestate.edu"));

			// msg.setReplyTo(InternetAddress.parse(
			// "do-not-reply@seal.boisestate.edu", false));

			msg.setSubject(subject, "UTF-8");

			// msg.setText(body, "UTF-8");
			// msg.setContent(body, "text/html; charset=utf-8");
			msg.setText(body, "utf-8", "html");

			msg.setSentDate(new Date());

			msg.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(toEmail, false));
			for (String email : emaillist) {
				msg.addRecipient(Message.RecipientType.BCC,
						new InternetAddress(email));
			}

			Transport.send(msg);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Java Program to Send Email with TLS Authentication
	public void sendMailWithGmailTLS(String toEmail, String subject, String body) {
		final String fromEmail = "noreplygpms@gmail.com"; // requires valid
		// gmail
		// id
		final String password = "gpmstest"; // correct password for gmail id

		// /////////////////////////////////////////////////////////////
		// IMPORTANT NOTE //////////////////////////////////////////////
		// /YOU MUST ENABLE LESS SECURE APPS FOR THIS TO WORK///////////
		// https://www.google.com/settings/u/1/security/lesssecureapps//
		// /////////////////////////////////////////////////////////////

		System.out.println("TLSEmail Start");
		properties = new Properties();
		properties.put("mail.smtp.host", "smtp.gmail.com"); // SMTP Host
		properties.put("mail.smtp.port", "587"); // TLS Port
		properties.put("mail.smtp.auth", "true"); // enable authentication
		properties.put("mail.smtp.starttls.enable", "true"); // enable STARTTLS

		// props1.put("mail.smtp.starttls.required", "true");

		// create Authenticator object to pass in Session.getInstance argument
		Authenticator auth = new Authenticator() {
			// override the getPasswordAuthentication method
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(fromEmail, password);
			}
		};
		System.out.println("Session created");

		Session session = Session.getInstance(properties, auth);

		sendEmail(session, toEmail, subject, body);
	}

	public void sendMailWithGmailSSL(String toEmail, String subject, String body) {
		final String fromEmail = "noreplygpms@gmail.com"; // requires valid
		// gmail
		// id
		final String password = "gpmstest"; // correct password for gmail id

		System.out.println("SSLEmail Start");
		properties = new Properties();
		properties.put("mail.smtp.host", "smtp.gmail.com"); // SMTP Host
		properties.put("mail.smtp.socketFactory.port", "465"); // SSL Port
		properties.put("mail.smtp.socketFactory.class",
				"javax.net.ssl.SSLSocketFactory"); // SSL Factory Class
		// props.put("mail.smtp.socketFactory.fallback", "true");

		properties.put("mail.smtp.auth", "true"); // Enabling SMTP
													// Authentication
		properties.put("mail.smtp.port", "465"); // SMTP Port

		Authenticator auth = new Authenticator() {
			// override the getPasswordAuthentication method
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(fromEmail, password);
			}
		};

		Session session = Session.getDefaultInstance(properties, auth);
		System.out.println("Session created");

		sendEmail(session, toEmail, subject, body);

	}

	/**
	 * Utility method to send simple HTML email
	 * 
	 * @param toEmail
	 * @param subject
	 * @param body
	 */
	public void sendEmail(Session session, String toEmail, String subject,
			String body) {
		try {
			// Create a default MimeMessage object.
			MimeMessage msg = new MimeMessage(session);

			// set message headers
			msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
			msg.addHeader("format", "flowed");
			msg.addHeader("Content-Transfer-Encoding", "8bit");

			msg.setFrom(new InternetAddress("do-not-reply@seal.boisestate.edu",
					"do-not-reply@seal.boisestate.edu"));

			// msg.setReplyTo(InternetAddress.parse(
			// "do-not-reply@seal.boisestate.edu", false));

			msg.setSubject(subject, "UTF-8");

			// msg.setText(body, "UTF-8");
			// msg.setContent(body, "text/html; charset=utf-8");
			msg.setText(body, "utf-8", "html");

			msg.setSentDate(new Date());

			msg.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(toEmail, false));

			Transport.send(msg);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Java Program to send Email with Attachment
	/**
	 * Utility method to send email with attachment
	 * 
	 * @param toEmail
	 * @param subject
	 * @param body
	 * @throws IOException
	 */
	public void sendAttachmentEmail(String toEmail, String subject,
			String body, String attachName) throws IOException {
		try {
			// Get the default Session object.
			Session session = Session.getDefaultInstance(properties);
			System.out.println("Session created");

			MimeMessage msg = new MimeMessage(session);
			msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
			msg.addHeader("format", "flowed");
			msg.addHeader("Content-Transfer-Encoding", "8bit");

			msg.setFrom(new InternetAddress("do-not-reply@seal.boisestate.edu",
					"do-not-reply@seal.boisestate.edu"));

			msg.setReplyTo(InternetAddress.parse(
					"do-not-reply@seal.boisestate.edu", false));

			msg.setSubject(subject, "UTF-8");

			msg.setSentDate(new Date());

			msg.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(toEmail, false));

			// Create the message body part
			MimeBodyPart messageBodyPart = new MimeBodyPart();

			// Fill the message
			messageBodyPart.setText(body, "utf-8", "html");

			// Create a multipart message for attachment
			Multipart multipart = new MimeMultipart();

			// Set text message part
			multipart.addBodyPart(messageBodyPart);

			// Second part is attachment
			messageBodyPart = new MimeBodyPart();

			messageBodyPart.attachFile(getFilePath());
			messageBodyPart.setFileName(attachName);
			multipart.addBodyPart(messageBodyPart);

			// Send the complete message parts
			msg.setContent(multipart);

			// Send message
			Transport.send(msg);
			System.out.println("EMail Sent Successfully with attachment!!");
		} catch (MessagingException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	// Java Program to send Email with Image
	/**
	 * Utility method to send image in email body
	 * 
	 * @param toEmail
	 * @param subject
	 * @param body
	 */
	public void sendImageEmail(String toEmail, String subject, String body,
			String attachName) {
		try {
			// Get the default Session object.
			Session session = Session.getDefaultInstance(properties);
			System.out.println("Session created");

			MimeMessage msg = new MimeMessage(session);
			msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
			msg.addHeader("format", "flowed");
			msg.addHeader("Content-Transfer-Encoding", "8bit");

			msg.setFrom(new InternetAddress("do-not-reply@seal.boisestate.edu",
					"do-not-reply@seal.boisestate.edu"));

			msg.setReplyTo(InternetAddress.parse(
					"do-not-reply@seal.boisestate.edu", false));

			msg.setSubject(subject, "UTF-8");

			msg.setSentDate(new Date());

			msg.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(toEmail, false));

			// Create the message body part
			MimeBodyPart messageBodyPart = new MimeBodyPart();

			messageBodyPart.setText(body, "utf-8", "html");

			// Create a multipart message for attachment
			Multipart multipart = new MimeMultipart();

			// Set text message part
			multipart.addBodyPart(messageBodyPart);

			// Second part is image attachment
			messageBodyPart = new MimeBodyPart();

			// String filename = EmailUtil.class.getClass()
			// .getResource("/teapot.jpg").getPath();
			DataSource source = new FileDataSource(getFilePath());
			messageBodyPart.setDataHandler(new DataHandler(source));
			messageBodyPart.setFileName(attachName);
			// Trick is to add the content-id header here
			messageBodyPart.setHeader("Content-ID", "image_id");
			multipart.addBodyPart(messageBodyPart);

			// third part for displaying image in the email body
			messageBodyPart = new MimeBodyPart();
			messageBodyPart.setContent("<h1>Attached Image</h1>"
					+ "<img src='cid:image_id'>", "text/html");
			multipart.addBodyPart(messageBodyPart);

			// Set the multipart message to the email message
			msg.setContent(multipart);

			// Send message
			Transport.send(msg);
			System.out.println("EMail Sent Successfully with image!!");
		} catch (MessagingException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
}
