package org.hrsys;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@MapperScan({"org.hrsys.mapper","org.hrsys.dao"})
@EnableCaching
public class HrserverApplication {

	public static void main(String[] args) {
		SpringApplication.run(HrserverApplication.class, args);
	}
}
