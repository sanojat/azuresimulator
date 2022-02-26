package com.azure.learn.simulator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

/**
 * @author SANOJ
 *
 */
@SpringBootApplication
public class AzureSimulator {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SpringApplication.run(AzureSimulator.class, args);
	}
}
