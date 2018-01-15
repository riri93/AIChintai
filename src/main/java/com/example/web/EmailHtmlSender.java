package com.example.web;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EmailHtmlSender {

	@Autowired
	private EmailSender emailSender;

	public void send(String to, String subject,String body) throws MessagingException {
		
		 emailSender.sendHtml(to, subject, body);
	}
}