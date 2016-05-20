package com.scraping.application;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;

@Component
public class Scraper {

	@Autowired
	private ScraperSettings config;
	
	@Autowired
	private RestTemplate template;

	/**
	 * Request page to scrape
	 * @return html response
	 */
	private ResponseEntity<String> download() {
		ResponseEntity<String> entity = template.getForEntity(config.getUrl(), String.class);
		return entity;
	}
	
	/**
	 * Get list of elements to be parsed
	 * @return elements to be parsed
	 */
	public List<Element> getElements() {
		
		String html = this.download().getBody();
		final Document htmlDoc = Jsoup.parse(html);
		
		List<Element> elements = 
					htmlDoc.select(config.getProducts())
							.stream()
							.filter(p -> p instanceof Element)
							.map(p -> (Element) p)
							.collect(Collectors.toList());
		
		return elements;
	}
	
	/**
	 * Retrieve the list of products
	 * @return
	 */
	private List<Product> scrape() {

		List<Product> products = new ArrayList<Product>();
		final List<Element> anchorNodes = getElements();
		
		for (Element node : anchorNodes) {

			Node n = new Node((Element)node, config);
			
			Product p = new Product(n.getName(), n.getSize(), n.getPrice(), n.getDescription());
				
			products.add(p);
		}		
		
		return products;
	}
	
	/**
	 * Build the json result bean
	 * @return Result bean containing products and execution time
	 */
	public Result getResult() throws JsonProcessingException {
		
		Instant start = Instant.now();

		List<Product> products = this.scrape();
    	
    	Instant end = Instant.now();
    	
    	
    	return new Result(products, Duration.between(start, end).toMillis()/1000.0);
	}
}
