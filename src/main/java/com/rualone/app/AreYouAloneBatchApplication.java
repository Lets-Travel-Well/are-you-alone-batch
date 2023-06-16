package com.rualone.app;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableBatchProcessing
@EnableScheduling
@SpringBootApplication
public class AreYouAloneBatchApplication {

	public static void main(String[] args) {
		SpringApplication.run(AreYouAloneBatchApplication.class, args);
	}

}
