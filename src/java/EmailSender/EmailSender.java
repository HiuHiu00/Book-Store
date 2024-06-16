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

public class EmailSender {

    private final String FROM_EMAIL = "clothingshoponlineg1se1754@gmail.com";
    private final String EMAIL_PASSWORD = "pizwgjrviipmttyx";

    public void sendMsgEmail(String toEmail, String subject) {
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

            String htmlContent = builder();
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
        es.sendMsgEmail("hieulove0408@gmail.com", "Test");
    }

    public String builder() {
        String filePath = "web/views/EmailSenderForm.html";

        StringBuilder content = new StringBuilder();

        try ( BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return replace(content.toString());

    }

    public String replace(String content) {
        String updatedContent = content.replace("XXXXXX", "123321");
    updatedContent = updatedContent.replace("timeSentForm", "12Nov, 2000");

        return updatedContent;
    }
}
