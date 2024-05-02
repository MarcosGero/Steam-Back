package com.steamer.capas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
public class SteamerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SteamerApplication.class, args);
	}

}
