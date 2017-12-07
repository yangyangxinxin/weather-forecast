package com.luckysweetheart.weatherforecast.email;

import com.luckysweetheart.weatherforecast.util.DateUtil;
import freemarker.template.Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.annotation.Resource;
import javax.mail.*;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Map;

import static org.springframework.util.Assert.isTrue;
import static org.springframework.util.Assert.notNull;

/**
 * 邮件服务，可用来找回密码、发送验证码。
 * Created by yangxin on 2017/6/16.
 */
@Service
public class EmailService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Qualifier("getSender")
    @Resource
    private JavaMailSender javaMailSender;

    @Resource
    private FreeMarkerConfigurer freeMarkerConfigurer;

    /**
     * 发送纯文本的简单邮件
     *
     * @param to
     * @param subject
     * @param content
     */
    public void sendSimpleMail(String to, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(content);


        try {
            javaMailSender.send(message);
            logger.info("简单邮件已经发送。");
        } catch (Exception e) {
            logger.error("发送简单邮件时发生异常！", e);
        }
    }


    public synchronized void sendEmailTemplate(EmailSender emailSender) {
        long start = System.currentTimeMillis();
        logger.info("开始发送邮件 at {}", DateUtil.formatNow());
        MimeMessage message = null;
        try {
            isTrue(emailSender.getSendToArray() != null && emailSender.getSendToArray().length > 0, "收件人不能为空");
            notNull(emailSender.getSubject(), "邮件主题或标题不能为空");

            message = javaMailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(from);
            helper.setTo(emailSender.getSendToArray());
            helper.setSubject(emailSender.getSubject());

            Map<String, Object> model = emailSender.getParam();

            Template template = freeMarkerConfigurer.getConfiguration().getTemplate(emailSender.getEmailTemplate().getPath());
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
            helper.setText(html, true);
            javaMailSender.send(message);

            long end = System.currentTimeMillis();
            logger.info("邮件发送成功 ，耗时 {} s", (end - start) / 1000);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        }
    }


   /* //用户名密码验证，需要实现抽象类Authenticator的抽象方法PasswordAuthentication
    static class MyAuthenricator extends Authenticator {
        String u = null;
        String p = null;

        public MyAuthenricator(String u, String p) {
            this.u = u;
            this.p = p;
        }

        @Override
        public PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(u, p);
        }
    }*/

    @Value("${spring.mail.username}")
    private String account;//登录用户名
    @Value("${spring.mail.password}")
    private String pass;        //登录密码
    @Value("${spring.mail.username}")
    private String from;        //发件地址
    @Value("${spring.mail.host}")
    private String host;        //服务器地址
    @Value("${spring.mail.port}")
    private String port;        //端口
    @Value("${spring.mail.protocol}")
    private String protocol; //协议

   /* public void send() {
        Properties prop = new Properties();
        //协议
        prop.setProperty("mail.transport.protocol", protocol);
        //服务器
        prop.setProperty("mail.smtp.host", host);
        //端口
        prop.setProperty("mail.smtp.port", port);
        //使用smtp身份验证
        prop.setProperty("mail.smtp.auth", "true");
        //使用SSL，企业邮箱必需！
        //开启安全协议
        MailSSLSocketFactory sf = null;
        try {
            sf = new MailSSLSocketFactory();
            sf.setTrustAllHosts(true);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        prop.put("mail.smtp.ssl.enable", "true");
        prop.put("mail.smtp.ssl.socketFactory", sf);
        Session session = Session.getDefaultInstance(prop, new MyAuthenricator(account, pass));
        session.setDebug(true);


        MimeMessage mimeMessage = new MimeMessage(session);
        try {
            *//*mimeMessage.setFrom(new InternetAddress(from, "XXX"));
            mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress("981987024@qq.com"));
            mimeMessage.setSubject("XXX账户密码重置");
            mimeMessage.setSentDate(new Date());
            mimeMessage.setText("您在XXX使用了密码重置功能，请点击下面链接重置密码:\n"
                    + "http://localhost:/XXX/ResetPassword?id=123456789");
            mimeMessage.saveChanges();*//*

            String str = "<div style=\"width: 100%;\">\n" +
                    "    <div style=\"margin-left: 20px;font-size: 14px;color: black;\">\n" +
                    "        <p >亲爱的会员您好：</p>\n" +
                    "        <p >感谢您注册本网站账户，您的邮箱帐号是<span style=\"color:#999999;\">（请牢记您的注册信息。）</span></p>\n" +
                    "        <p style=\"margin-top: 35px;margin-bottom: 0px;\">请点击下面的链接来激活您的账户：</p>\n" +
                    "        <p style=\"margin: 0px;\"><a href=\"\"></a></p>\n" +
                    "        <p style=\"color:#999999;\">(如果以上链接无法点击, 请将上面的地址复制到你的浏览器(如IE)的地址栏进入)</p>\n" +
                    "        <p style=\"margin-bottom: 0px;margin-top: 20px;\"> 此致 </p>\n" +
                    "        <p ><a href=\"http://www.luckysweetheart.dev\">http://www.luckysweetheart.dev</a> yangxin </p>\n" +
                    "    </div>\n" +
                    "</div>";
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom(from);
            helper.setTo("981987024@qq.com");
            helper.setSubject("注册");
            helper.setText(str, true);
            //javaMailSender.send(mimeMessage);
            mimeMessage.getEncoding();
            Transport.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }*/
}
