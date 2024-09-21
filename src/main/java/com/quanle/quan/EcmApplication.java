package com.quanle.quan;

import com.quanle.quan.config.AppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
public class EcmApplication {

	public static void main(String[] args) {
		SpringApplication.run(EcmApplication.class, args);
	}

}
