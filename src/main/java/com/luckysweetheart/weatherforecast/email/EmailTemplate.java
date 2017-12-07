package com.luckysweetheart.weatherforecast.email;

/**
 * Created by yangxin on 2017/6/16.
 */
public enum EmailTemplate {

    FORGET_PASSWORD("/email/forget_password.ftl"),
    REGISTER("/email/register.ftl"),
    TEST("/email/test.ftl"),
    VIOLATION_COUNT("/email/violationCount.ftl");

    private String path;

    EmailTemplate(String path) {
        this.path = path;
    }

    public String getPath(){
        return this.path;
    }

}
