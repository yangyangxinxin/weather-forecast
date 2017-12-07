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
		weatherScheduleService.execute();
	}

}
