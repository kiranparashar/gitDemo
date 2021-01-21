package com.yatra.utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import javax.mail.*;
import javax.mail.internet.*;

public class EmailReport {
	
	private Properties props;
	private Session session;
	MimeMessage message;
	MimeMultipart multipart;
	MimeBodyPart messageBodyPart;
	private HashMap<String , String> messageContents;
	FileUtil fileUtil = new FileUtil();
	
	public EmailReport(){
		
	    session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(ReadProperties.getProperty("from"), ReadProperties.getProperty("password"));
			}
		});
	    
	    message = new MimeMessage(session);
	    multipart = new MimeMultipart();
	}
	
	public void setEmailProperties(){
		
		props = new Properties();
		props.put("mail.smtp.auth", ReadProperties.getProperty("mail.smtp.auth"));
		props.put("mail.smtp.starttls.enable", ReadProperties.getProperty("mail.smtp.starttls.enable"));
		props.put("mail.smtp.host", ReadProperties.getProperty("mail.smtp.host"));
		props.put("mail.smtp.port", ReadProperties.getProperty("mail.smtp.port"));
		// for outside network mail
		props.put("mail.smtp.socketFactory.class", ReadProperties.getProperty("mail.smtp.socketFactory.class"));
		props.put("mail.smtp.port", ReadProperties.getProperty("mail.smtp.port"));
	}
	
	@SuppressWarnings("rawtypes")
	public void addRecepients(HashMap<String , String> recepients) throws AddressException, MessagingException{
	
		 Set<Map.Entry<String, String>> recepientSet = recepients.entrySet();
		 for(Entry recepient : recepientSet){
			 switch(recepient.getKey().toString()){
			 case "to":
				 message.addRecipient(Message.RecipientType.TO, new InternetAddress(recepient.getValue().toString()));
				 break;
			 case "cc":
				 message.addRecipient(Message.RecipientType.CC, new InternetAddress(recepient.getValue().toString()));
				 break;
			 case "bcc":
				 message.addRecipient(Message.RecipientType.BCC, new InternetAddress(recepient.getValue().toString()));
				 break;	 
			 }
		 }

	}
	
	@SuppressWarnings("rawtypes")
	public void composeEmailMesaage(HashMap<String, String> messageContents ) throws MessagingException{
		
		Set<Map.Entry<String, String>> messageContentSet = messageContents.entrySet();
		
		for(Entry messageContent:messageContentSet){
			
			messageBodyPart = new MimeBodyPart();
			messageBodyPart.setContent("<BR>" + messageContent.getKey().toString(), messageContent.getValue().toString());
			multipart.addBodyPart(messageBodyPart);
		}
		messageBodyPart.setContent("<BR>" + "----------------Thanks !!----------------", "text/html");
		multipart.addBodyPart(messageBodyPart);
		message.setContent(multipart);
	}
	
	public void sendMailForTestFailure(String testCase, String superpnr, String testData, String mesg, HashMap<String ,String> recepients,
			String subject) throws IOException, InterruptedException {

		try {
			message.setFrom(new InternetAddress(ReadProperties.getProperty("from")));
			addRecepients(recepients);
			message.setSubject(subject);
			message.setText(mesg);

			messageContents.put(testCase, "text/html");
			messageContents.put(superpnr, "text/html");
			messageContents.put(testData, "text/html");
			messageContents.put(mesg, "text/html");
			
			composeEmailMesaage(messageContents);
		
			Transport.send(message);

		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

	public void sendTestExecutionReport(String testCase, String mesg, HashMap<String ,String> recepients, String subject)
			throws IOException, InterruptedException {
		
		// Compose the message
		try {
			message.setFrom(new InternetAddress(ReadProperties.getProperty("from")));
			addRecepients(recepients);
			message.setSubject(subject);
		
			messageContents.put(testCase, "text/html");
			messageContents.put(mesg, "text/html");
			
			if (fileUtil.getReportFilePath() != null) {
				MimeBodyPart attachPart = new MimeBodyPart();
				try {
					attachPart.attachFile(fileUtil.getReportFilePath());
				} catch (IOException e) {
					e.printStackTrace();
				}
				multipart.addBodyPart(attachPart);
			} else {
				
				messageContents.put("Sorry! somthing went wrong, report NOT generated.", "text/html");
			}
			composeEmailMesaage(messageContents);
			message.setContent(multipart);
			Transport.send(message);

		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
}
