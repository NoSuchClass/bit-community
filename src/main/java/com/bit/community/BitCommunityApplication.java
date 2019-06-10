package com.bit.community;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(FreeMarkerProperties.class)
public class BitCommunityApplication {
	public static void main(String[] args) {
		SpringApplication.run(BitCommunityApplication.class, args);
	}

}
