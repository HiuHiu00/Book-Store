/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
package EmailSender;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;
import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.text.SimpleDateFormat;
import java.util.Date;
import jakarta.servlet.ServletContext;

public class EmailSender {

    private final String FROM_EMAIL = "clothingshoponlineg1se1754@gmail.com";
    private final String EMAIL_PASSWORD = "pizwgjrviipmttyx";

    public void sendMsgEmail(ServletContext context, String toEmail, String subject, String type) {
        Properties props = System.getProperties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.user", FROM_EMAIL);
        props.put("mail.smtp.password", EMAIL_PASSWORD);
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getDefaultInstance(props);

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(FROM_EMAIL));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
            message.setSubject(subject);
            //message.setText(msg);

            String htmlContent = getEmailSenderFormat(context, toEmail, type);
            message.setContent(htmlContent, "text/html");
            Transport transport = session.getTransport("smtp");
            transport.connect("smtp.gmail.com", FROM_EMAIL, EMAIL_PASSWORD);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        EmailSender es = new EmailSender();
//        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
//        scheduler.schedule(() -> {
//                es.sendMsgEmail("hieulove0408@gmail.com", "Test");
//        }, 3, TimeUnit.SECONDS);
        //es.sendMsgEmail("hieulove0408@gmail.com", "Test");

    }

    public String getEmailSenderFormat(ServletContext context, String toEmail, String type) {
        String filePath = context.getRealPath("views/EmailSenderForm.html");

        StringBuilder content = new StringBuilder();

        try ( BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        String updatedContent ="";
        if (type.equals("ocType")) {
             updatedContent =replaceAttributeForOTPCode(content.toString(), toEmail);
        }
        if (type.equals("ocType")) {
             updatedContent =replaceAttributeForNewPassword(content.toString(), toEmail);
        }
        return updatedContent;
    }

    public String replaceAttributeForOTPCode(String content, String toEmail) {
        String updatedContent = content.replace("XXXXXX", "123321");

        Date now = new Date();
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        String time = timeFormat.format(now);
        SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMM,yyyy");
        String date = dateFormat.format(now);
        updatedContent = updatedContent.replace("timeSentForm", time);
        updatedContent = updatedContent.replace("dateSentForm", date);

        updatedContent = updatedContent.replace("recipient's_email", toEmail);

        updatedContent = updatedContent.replace("getOtpNotification", " Thank you for choosing MyBookStore. Use the following OTP to complete the procedure to get your new password. OTP is valid for\n"
                + "                            <span style=\"font-weight: 600; color: #1f1f1f;\">5 minutes</span>.\n"
                + "                            Do not share this code with others.");
        return updatedContent;
    }
    
    public String replaceAttributeForNewPassword(String content, String toEmail) {
        String updatedContent = content.replace("XXXXXX", "123321");

        Date now = new Date();
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        String time = timeFormat.format(now);
        SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMM,yyyy");
        String date = dateFormat.format(now);
        updatedContent = updatedContent.replace("timeSentForm", time);
        updatedContent = updatedContent.replace("dateSentForm", date);

        updatedContent = updatedContent.replace("recipient's_email", toEmail);

        updatedContent = updatedContent.replace("getOtpNotification", " Thank you for choosing MyBookStore. Use the following OTP to complete the procedure to get your new password. OTP is valid for\n"
                + "                            <span style=\"font-weight: 600; color: #1f1f1f;\">5 minutes</span>.\n"
                + "                            Do not share this code with others.");
        return updatedContent;
    }
}
