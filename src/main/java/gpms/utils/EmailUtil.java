package gpms.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class EmailUtil {

	private static String filePath = new String();

	public static String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		EmailUtil.filePath = filePath;
	}

	public EmailUtil() {
	}

	public EmailUtil(String attachmentFile) throws Exception {
		this.setFilePath(this.getClass().getResource(attachmentFile).toURI()
				.getPath());
	}

	/**
	 * Utility method to send simple HTML email
	 * 
	 * @param session
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

			msg.setFrom(new InternetAddress("no_reply@seal.boisestate.edu",
					"NoReply-GPMS"));

			msg.setReplyTo(InternetAddress.parse(
					"no_reply@seal.boisestate.edu", false));

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
	 * @param session
	 * @param toEmail
	 * @param subject
	 * @param body
	 * @throws IOException
	 */
	public void sendAttachmentEmail(Session session, String toEmail,
			String subject, String body, String attachName) throws IOException {
		try {
			MimeMessage msg = new MimeMessage(session);
			msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
			msg.addHeader("format", "flowed");
			msg.addHeader("Content-Transfer-Encoding", "8bit");

			msg.setFrom(new InternetAddress("no_reply@seal.boisestate.edu",
					"NoReply-GPMS"));

			msg.setReplyTo(InternetAddress.parse(
					"no_reply@seal.boisestate.edu", false));

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
	 * @param session
	 * @param toEmail
	 * @param subject
	 * @param body
	 */
	public void sendImageEmail(Session session, String toEmail, String subject,
			String body, String attachName) {
		try {
			MimeMessage msg = new MimeMessage(session);
			msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
			msg.addHeader("format", "flowed");
			msg.addHeader("Content-Transfer-Encoding", "8bit");

			msg.setFrom(new InternetAddress("no_reply@seal.boisestate.edu",
					"NoReply-GPMS"));

			msg.setReplyTo(InternetAddress.parse(
					"no_reply@seal.boisestate.edu", false));

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
