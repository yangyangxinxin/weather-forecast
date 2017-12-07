package com.luckysweetheart.weatherforecast.schedule;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.luckysweetheart.weatherforecast.dto.Weather;
import com.luckysweetheart.weatherforecast.email.EmailSender;
import com.luckysweetheart.weatherforecast.email.EmailTemplate;
import com.luckysweetheart.weatherforecast.util.DateUtil;
import com.luckysweetheart.weatherforecast.util.HttpUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yangxin on 2017/12/7.
 */
@Configuration
@EnableScheduling
public class WeatherScheduleService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${api.host}")
    private String host;

    @Value("${api.path}")
    private String path;

    @Value("${api.appcode}")
    private String appcode;

    @Value("${send.emails}")
    private String emails;

    @Value("${lat}")
    private String lat;

    @Value("${lon}")
    private String lon;

    private Weather getWeather() {
        // 29.5195824907,106.5151810924 重庆谢家湾
        // 43.9195073769,87.6246873572 乌鲁木齐永祥街
        String method = "POST";
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        //根据API的要求，定义相对应的Content-Type
        headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        Map<String, String> querys = new HashMap<String, String>();
        Map<String, String> bodys = new HashMap<String, String>();
        bodys.put("lat", lat);
        bodys.put("lon", lon);

        try {
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
            HttpEntity entity = response.getEntity();
            InputStream content = entity.getContent();
            StringBuilder sb = new StringBuilder();
            List<String> strings = IOUtils.readLines(content, "UTF-8");
            for (String string : strings) {
                sb.append(string);
            }
            // {"code":0,"data":{"city":{"cityId":284609,"counname":"中国","name":"东城区","pname":"北京市","timezone":"8"},"forecast":[{"conditionDay":"多云","conditionIdDay":"1","conditionIdNight":"30","conditionNight":"晴","predictDate":"2017-12-07","tempDay":"5","tempNight":"-5","updatetime":"2017-12-07 11:10:07","windDirDay":"西北风","windDirNight":"西南风","windLevelDay":"4","windLevelNight":"3"},{"conditionDay":"晴","conditionIdDay":"0","conditionIdNight":"30","conditionNight":"大部晴朗","predictDate":"2017-12-08","tempDay":"6","tempNight":"-3","updatetime":"2017-12-07 11:10:07","windDirDay":"西南风","windDirNight":"西北风","windLevelDay":"3","windLevelNight":"2"},{"conditionDay":"多云","conditionIdDay":"1","conditionIdNight":"31","conditionNight":"少云","predictDate":"2017-12-09","tempDay":"6","tempNight":"-2","updatetime":"2017-12-07 11:10:07","windDirDay":"西南风","windDirNight":"西北风","windLevelDay":"2","windLevelNight":"3"}]},"msg":"success","rc":{"c":0,"p":"success"}}
            System.out.println(sb.toString());
            return getWeather(sb.toString());
            //获取response的body
            //System.out.println(EntityUtils.toString(response.getEntity()));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    private Weather getWeather(String result) {
        Weather weather = new Weather();
        if (StringUtils.isNotBlank(result)) {
            JSONObject jsonObject = JSON.parseObject(result);
            if (jsonObject != null) {
                Integer code = jsonObject.getInteger("code");
                if (code == 0) {

                    JSONObject data = jsonObject.getJSONObject("data");

                    JSONObject cityObject = data.getJSONObject("city");
                    String city = cityObject.getString("counname") + " " + cityObject.getString("pname") + " " + cityObject.getString("name");
                    weather.setCity(city);
                    JSONArray forecast = data.getJSONArray("forecast");

                    if (forecast != null && forecast.size() > 0) {
                        JSONObject o = (JSONObject) forecast.get(0);

                        String conditionDay = o.getString("conditionDay");

                        String tempDay = o.getString("tempDay");

                        String tempNight = o.getString("tempNight");

                        weather.setConditionDay(conditionDay);
                        weather.setTempDay(tempDay);
                        weather.setTempNight(tempNight);

                    }


                }
            }
        }
        return weather;
    }

    private void sendEmail(Weather weather) {
        String[] emailAddress = emails.split(";");
        String subject = DateUtil.formatDate(new Date(), "yyyy年MM月dd日") + "天气提醒";
        EmailSender.init().to(emailAddress).subject(subject).param("weather", weather).param("now", DateUtil.formatDate(new Date(), "yyyy年MM月dd日")).emailTemplate(EmailTemplate.TEST).sleep(true).send();
    }

    /**
     * 6:30执行任务
     */
    @Scheduled(cron = "00 08 14 * * ?")
    public void execute() {
        Weather weather = getWeather();
        if (weather == null) {
            logger.error("天气获取失败");
            return;
        }
        sendEmail(weather);
    }
}
