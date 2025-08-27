package ps.demo.lqbs.test;

import ps.demo.commonlibx.common.SettingTool;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class EmailSender {

    public static void main(String[] args) {
        // 邮件服务器配置
        String host = "smtp.example.com"; // SMTP服务器地址
        String username = "your-email@example.com"; // 发件人邮箱
        String password = SettingTool.getConfigByKey("emailpassword"); // 邮箱密码或授权码

        // 收件人邮箱
        String to = "recipient@example.com";

        // 设置邮件属性
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true"); // 启用认证
        props.put("mail.smtp.starttls.enable", "true"); // 启用TLS加密
        props.put("mail.smtp.host", host); // SMTP服务器
        props.put("mail.smtp.port", "587"); // SMTP端口

        // 创建会话
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            // 创建邮件
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject("Java邮件测试");
            message.setText("这是一封通过Java程序发送的测试邮件。");

            // 发送邮件
            Transport.send(message);

            System.out.println("邮件发送成功！");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
