package com.gelura.vaporback;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
public class VaporBackApplication {

	public static void main(String[] args) {
		SpringApplication.run(VaporBackApplication.class, args);
	}

}
