package utils;

import javax.mail.*;
import javax.mail.internet.*;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class MailUtils {

    // 📧 Cấu hình tài khoản gửi (dùng App Password của Gmail)
    private static final String SMTP_USER = "datdatphuoc@gmail.com";   // 📬 Gmail gửi
    private static final String SMTP_PASS = "erhk kguv lzqv mkas";      // 🔑 App password

    /**
     * Gửi email HTML đơn giản.
     * @param to       Email người nhận
     * @param subject  Tiêu đề
     * @param htmlBody Nội dung HTML
     */
    public static void sendEmail(String to, String subject, String htmlBody) {
        try {
            // ⚙️ Cấu hình SMTP
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");
            props.put("mail.smtp.ssl.protocols", "TLSv1.2");

            // 📤 Phiên đăng nhập SMTP
            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(SMTP_USER, SMTP_PASS);
                }
            });

            // ✉️ Soạn email
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(SMTP_USER, "📢 Trang Tin", StandardCharsets.UTF_8.name()));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to, false));
            message.setSubject(subject, StandardCharsets.UTF_8.name());
            message.setContent(htmlBody, "text/html; charset=UTF-8");
            message.setReplyTo(new Address[]{new InternetAddress(SMTP_USER)});

            // 📬 Gửi đi
            Transport.send(message);
            System.out.println("✅ Đã gửi email tới: " + to);

        } catch (Exception e) {
            System.err.println("❌ Gửi email thất bại tới: " + to);
            e.printStackTrace();
        }
    }
}
