//package com.example.web;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.mail.javamail.MimeMessageHelper;
//import org.springframework.stereotype.Component;
//
//import javax.mail.MessagingException;
//import javax.mail.internet.MimeMessage;
// 
//@Component
//public class EmailSender {
// 
//    private static final Logger LOGGER = LoggerFactory.getLogger(EmailSender.class);
// 
//    @Autowired
//    private JavaMailSender javaMailSender;
// 
//    public void sendPlainText(String to, String subject, String text) throws MessagingException {
//      sendM(to, subject, text, false);
//    }
// 
//    public void sendHtml(String to, String subject, String htmlBody) throws MessagingException {
//         sendM(to, subject, htmlBody, true);
//    }
// 
//    private void sendM(String to, String subject, String text, Boolean isHtml) throws MessagingException {
//       
//            MimeMessage mail = javaMailSender.createMimeMessage();
//            MimeMessageHelper helper = new MimeMessageHelper(mail, true);
//            mail.setFrom("no-reply@hrdatabank.com");
//            helper.setTo(to);
//            helper.setSubject(subject);
//            helper.setText(text, isHtml);
//            javaMailSender.send(mail);
//            LOGGER.info("Send email '{}' to: {}", subject, to);
//           // return new EmailStatus(to, subject, text).success();
//       
////        	System.out.println("---"+e.getMessage());
////            LOGGER.error(String.format("Problem with sending email to: {}, error message: {}", to, e.getMessage()));
////            //return new EmailStatus(to, subject, text).error(e.getMessage());
//        
//    }
//}