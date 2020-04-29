package com.delta.kylintask;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.delta.kylintask.mapper")
public class KylinTaskApplication {

	public static void main(String[] args) {
		SpringApplication.run(KylinTaskApplication.class, args);
	}

}
