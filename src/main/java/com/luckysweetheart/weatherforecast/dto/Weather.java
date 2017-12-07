package com.luckysweetheart.weatherforecast.dto;

import java.io.Serializable;

/**
 * Created by yangxin on 2017/12/7.
 */
public class Weather implements Serializable {

    /**
     * 城市
     */
    private String city;

    /**
     * 白天温度
     */
    private String tempDay;

    /**
     * 夜间温度
     */
    private String tempNight;

    /**
     * 天气状况，晴、多云、雨 等
     */
    private String conditionDay;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getTempDay() {
        return tempDay;
    }

    public void setTempDay(String tempDay) {
        this.tempDay = tempDay;
    }

    public String getTempNight() {
        return tempNight;
    }

    public void setTempNight(String tempNight) {
        this.tempNight = tempNight;
    }

    public String getConditionDay() {
        return conditionDay;
    }

    public void setConditionDay(String conditionDay) {
        this.conditionDay = conditionDay;
    }

    @Override
    public String toString() {
        return "Weather{" +
                "city='" + city + '\'' +
                ", tempDay='" + tempDay + '\'' +
                ", tempNight='" + tempNight + '\'' +
                ", conditionDay='" + conditionDay + '\'' +
                '}';
    }
}
