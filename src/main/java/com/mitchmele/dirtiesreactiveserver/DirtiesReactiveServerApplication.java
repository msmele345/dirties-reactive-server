package com.mitchmele.dirtiesreactiveserver;

import com.mitchmele.dirtiesreactiveserver.repository.PottyEventRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
public class DirtiesReactiveServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(DirtiesReactiveServerApplication.class, args);
	}

}
