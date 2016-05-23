package com.segmenter.application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan({ "com.segmenter.application"})
public class Application implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(Application.class);

	@Autowired
	private Segmenter segmenter;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args).close();
    }
 
	/**
	 * Application main.
     * Read parameters if present otherwise use those in application.properties pointing to http://localhost:8080/
     *
	 */
    @Override
    public void run(String... args) throws Exception {

		String baseUrl;
		String filesNumber;

        // Take two parameters base_url, files_number from command line if present
        if (args.length >= 2) {
            baseUrl = args[0];
            filesNumber = args[1];
        } else {
            log.info("No valid parameters (baseUrl, filesNumber). Getting default ones.");

            // Otherwise it takes the default one in application.properties (used for testing purpose)
    		baseUrl = segmenter.getConfig().getBaseUrl();
            filesNumber = String.valueOf(segmenter.getConfig().getFiles());
    	}

        // throws UnsupportedOperationException if not valid
        SegmenterSettings.isBaseUrlValid(baseUrl);
        int files = SegmenterSettings.isFilesNumberValid(filesNumber);

     	segmenter.run(baseUrl, files);
    }

}