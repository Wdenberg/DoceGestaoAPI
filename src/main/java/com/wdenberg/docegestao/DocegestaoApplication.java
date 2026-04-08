package com.wdenberg.docegestao;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan("com.wdenberg.docegestao.security.config")
public class DocegestaoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DocegestaoApplication.class, args);
	}

}
