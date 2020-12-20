package com.codeforgeyt.wschatapplication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication
public class WsChatApplication {

	public static void main(String[] args) {
		SpringApplication.run(WsChatApplication.class, args);
	}

}
