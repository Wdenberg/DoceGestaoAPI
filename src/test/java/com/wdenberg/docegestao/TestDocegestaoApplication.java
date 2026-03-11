package com.wdenberg.docegestao;

import org.springframework.boot.SpringApplication;

public class TestDocegestaoApplication {

	public static void main(String[] args) {
		SpringApplication.from(DocegestaoApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
