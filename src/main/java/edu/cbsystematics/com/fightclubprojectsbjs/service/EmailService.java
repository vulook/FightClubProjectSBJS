package edu.cbsystematics.com.fightclubprojectsbjs.service;

import edu.cbsystematics.com.fightclubprojectsbjs.model.Mail;
import edu.cbsystematics.com.fightclubprojectsbjs.model.User;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;


public interface EmailService {

	// Sends an email
	void sendEmail(Mail mail);

}