package com.scraping.application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootApplication
@ComponentScan({ "com.scraping.application"})
public class Application implements CommandLineRunner {
	private static final Logger log = LoggerFactory.getLogger(Application.class);
	
	@Autowired
	private Scraper scraper;

	public static void main(String[] args) {
        SpringApplication.run(Application.class, args).close();
    }
 
	/**
	 * Application main
	 */
    @Override
    public void run(String... args) throws Exception {
    	
    	ObjectMapper mapper = new ObjectMapper();
    	String json = mapper.writeValueAsString(scraper.getResult());
    			
    	System.out.println(json);
    }
    
    @Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}