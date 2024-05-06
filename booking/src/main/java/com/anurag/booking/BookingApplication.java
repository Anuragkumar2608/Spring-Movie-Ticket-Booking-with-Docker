package com.anurag.booking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication //Tells the system that this is a Spring application and starting point of the application.
public class BookingApplication {

	public static void main(String[] args) {
		//Tells spring to start this application and create a servlet container and host this application on the servlet container.
		SpringApplication.run(BookingApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

}
