package gpms.model;

import gpms.utils.EmailUtil;

public class TestSendingJavaMail {

	public static void main(String[] args) throws Exception {

		// Java Program to send Email using SMTP without authentication
		System.out.println("SimpleEmail Start");

		final String toEmail = "milsonmun@yahoo.com"; // can be any email id

		EmailUtil emailUtil = new EmailUtil();

		emailUtil.sendMailWithoutAuth(toEmail, "SimpleEmail Testing Subject",
				"SimpleEmail Testing Body");

		// Java Program to send Email with Attachment
		emailUtil = new EmailUtil("/XACMLDatasheet.xls");
		emailUtil.sendAttachmentEmail(toEmail,
				"SSLEmail Testing Subject with Attachment",
				"<h1>SSLEmail Testing Body with Attachment</h1>",
				"rulemapping.xls");

		// Java Program to send Email with Image Embedded
		emailUtil = new EmailUtil("/teapot.jpg");
		emailUtil.sendImageEmail(toEmail,
				"SSLEmail Testing Subject with Image",
				"<h1>SSLEmail Testing Body with Image</h1>", "teapot.jpg");

		// Java Program to Send Email with TLS Authentication
		/**
		 * Outgoing Mail (SMTP) Server requires TLS or SSL: smtp.gmail.com (use
		 * authentication) Use Authentication: Yes Port for TLS/STARTTLS: 587
		 */

		emailUtil.sendMailWithGmailTLS(toEmail, "TLSEmail Testing Subject",
				"TLSEmail Testing Body");

		// Java Program to send Email with SSL Authentication
		/**
		 * Outgoing Mail (SMTP) Server requires TLS or SSL: smtp.gmail.com (use
		 * authentication) Use Authentication: Yes Port for SSL: 465
		 */

		emailUtil.sendMailWithGmailSSL(toEmail, "SSLEmail Testing Subject",
				"SSLEmail Testing Body");

	}
}
