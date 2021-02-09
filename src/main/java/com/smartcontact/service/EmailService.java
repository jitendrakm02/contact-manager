package com.smartcontact.service;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.stereotype.Service;

@Service
public class EmailService {
	public boolean sendEmail(String subject,String message,String to) {
		
		boolean f=false;
		
		String from="your email id";
		//variable for gmail host
		String host="smtp.gmail.com";
		
		Properties properties = System.getProperties();
		System.out.println("Properties : "+properties);
		
		//host set
		
				properties.put("mail.smtp.host", host);
				properties.put("mail.smtp.port", "465");
				properties.put("mail.smtp.ssl.enable", "true");
				properties.put("mail.smtp.auth", "true");
		
		//session object 
	    Session session=Session.getInstance(properties, new Authenticator() {

			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				// TODO Auto-generated method stub
				return new PasswordAuthentication("your mail id","your gmail password");
			}
			
		});
		//debug session
	    session.setDebug(true);
	    //compose the test message
	    
	    MimeMessage mimeMessage = new MimeMessage(session);
	    try {
			//from email to send mail
		   mimeMessage.setFrom();
		 //adding recipient email to send mail
		   mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
		   
		   //adding subject to email
		   mimeMessage.setSubject(subject);
		   //adding test to message
		   //mimeMessage.setText(message);
		   mimeMessage.setContent(message,"text/html");
		   
		   //send email by transport class
		   
		   Transport.send(mimeMessage);
		   System.out.println(" sent successfully.............");
		    f= true;
		}catch(Exception e) {
			e.printStackTrace();
		}	
		return f;
		
	}

}
