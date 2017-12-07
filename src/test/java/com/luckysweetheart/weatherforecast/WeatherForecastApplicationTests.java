package com.luckysweetheart.weatherforecast;

import com.luckysweetheart.weatherforecast.schedule.WeatherScheduleService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WeatherForecastApplicationTests {

	@Resource
	private WeatherScheduleService weatherScheduleService;

	@Test
	public void contextLoads() {
	}

	@Test
	public void testW(){
		// weather-forecast-0.0.1-SNAPSHOT.jar
		// nohup java -jar weather-forecast-0.0.1-SNAPSHOT.jar >weather-forecast.log &
		// ps -aux|grep weather-forecast-0.0.1-SNAPSHOT.jar
		weatherScheduleService.execute();
	}

}
