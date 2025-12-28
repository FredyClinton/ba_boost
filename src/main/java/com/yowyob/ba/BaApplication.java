package com.yowyob.ba;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class BaApplication {

	public static void main(String[] args) {
		SpringApplication.run(BaApplication.class, args);
	}

}
