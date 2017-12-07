package com.luckysweetheart.weatherforecast.email;


import com.luckysweetheart.weatherforecast.util.SpringUtil;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 邮件发送工具
 * Created by yangxin on 2017/6/16.
 */
public class EmailSender implements Serializable {

    /**
     * 邮件发送模板，本工具类封装的是采用Freemarker模板进行发送邮件，所以一定要指定这个值。
     */
    private EmailTemplate emailTemplate;

    /**
     * 邮件主题（标题）
     */
    private String subject;

    /**
     * 邮件接收方
     */
    private List<String> sendTo;

    /**
     * Freemarker变量参数
     */
    private Map<String, Object> param;

    /**
     * 发送邮件所需依赖
     */
    private EmailService emailService;

    /**
     * 邮件发送中是否进行睡眠，因为邮件发送的过程需要一定的时间，我无法确定是多久，有时几秒，有时几十秒，所以另起一个线程来发送邮件。
     * 在jUnit测试中，执行完以后，如果线程不阻塞，系统将无法发送邮件。用SpringBoot容器启动的，这个就可以避免了。<b>所以在用单元测试的时候，一定要指定这个值为true</b>
     */
    private boolean sleep = false;

    private long sleepTime = 1000 * 60; // 一分钟

    private ExecutorService fixedThreadPool = Executors.newFixedThreadPool(5);

    private EmailSender() {

    }

    public static EmailSender init() {
        return new EmailSender();
    }

    public EmailSender sleep() {
        return this.sleep(true);
    }

    public EmailSender sleep(boolean sleep) {
        this.sleep = sleep;
        return this;
    }

    public EmailSender sleep(boolean sleep, long sleepTime) {
        this.sleep = sleep;
        this.sleepTime = sleepTime;
        return this;
    }

    public EmailSender to(List<String> to) {
        this.sendTo = to;
        return this;
    }

    public EmailSender to(String to) {
        if (sendTo == null) {
            sendTo = new ArrayList<>();
        }
        sendTo.add(to);
        return this;
    }

    public EmailSender to(String... to) {
        if (sendTo == null) {
            sendTo = new ArrayList<>();
        }
        sendTo.addAll(Arrays.asList(to));
        return this;
    }

    public EmailSender subject(String subject) {
        this.subject = subject;
        return this;
    }

    public EmailSender param(Map<String, Object> param) {
        this.param = param;
        return this;
    }

    public EmailSender param(String key, Object value) {
        if (this.param == null) {
            this.param = new HashMap<>();
        }
        param.put(key, value);
        return this;
    }

    public EmailSender emailTemplate(EmailTemplate emailTemplate) {
        this.emailTemplate = emailTemplate;
        return this;
    }

    public EmailSender emailService(EmailService emailService) {
        this.emailService = emailService;
        return this;
    }


    public EmailTemplate getEmailTemplate() {
        return emailTemplate;
    }

    public String getSubject() {
        return subject;
    }

    public List<String> getSendTo() {
        return sendTo;
    }

    public Map<String, Object> getParam() {
        return param;
    }

    public String getSendToList() {
        if (this.sendTo == null || this.sendTo.size() == 0) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (String aSendTo : this.sendTo) {
            sb.append(aSendTo).append(";");
        }
        return sb.toString();
    }

    public String[] getSendToArray() {
        if (this.sendTo == null || this.sendTo.size() == 0) {
            return null;
        }
        try {
            String[] arr = new String[this.sendTo.size()];
            for (int i = 0, length = this.sendTo.size(); i < length; i++) {
                arr[i] = this.getSendTo().get(i);
            }
            return arr;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public synchronized void send(final EmailService emailService) {
        final EmailSender sender = this;
        this.fixedThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                emailService.sendEmailTemplate(sender);
            }
        });
        if (sleep) {
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private synchronized void initService() {
        if (this.emailService == null) {
            this.emailService = SpringUtil.getBean(EmailService.class);
        }
    }

    public synchronized void send() {
        initService();
        this.send(emailService);
    }
}
