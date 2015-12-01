package gpms.model;

import java.util.Properties;
import javax.mail.*;
import javax.mail.Session;
import javax.mail.internet.*;

/**
 * 
 * @author Thomas Volz
 *
 */
public class TestJavaMail {

	// /////////////////////////////////////////////////////////////
	// IMPORTANT NOTE //////////////////////////////////////////////
	// /YOU MUST ENABLE LESS SECURE APPS FOR THIS TO WORK///////////
	// https://www.google.com/settings/u/1/security/lesssecureapps//
	// /////////////////////////////////////////////////////////////

	// ////////////////////////////////////////////////////////////
	// You will need the files from the Javamail API///////////////
	// https://java.net/projects/javamail/pages/Home#Samples///////
	// ////////////////////////////////////////////////////////////

	public static void main(String args[]) {
		// Port is 465? 587?
		String to = "RECEIVEREMAILADDRESS"; // The address you are sending to
		Properties props = new Properties();
		// Start TTLS Lines added, required
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.starttls.required", "true");
		// End TTLS addition
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtop.socketFactory.port", "587");
		props.put("mail.smtp.socketFactory.class",
				"javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.socketFactory.fallback", "true");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "587");

		Session session = Session.getDefaultInstance(props,
				new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication("YOURGMAILADDRESS",
								"PASSWORD"); // Your
												// actual
												// email
												// address
												// and
												// password
					}
				});
		session.setDebug(true);
		try {
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress("SENDEREMAILADDRESS")); // your
																		// email
			// address
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(
					to));
			message.setSubject("Hello");
			message.setText("This is a test");
			Transport.send(message);
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}

	}
}
