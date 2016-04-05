/*
 * Copyright 2014 NAKANO Hideo.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
	private static final String DEFAULT_URL = "http://hiring-tests.s3-website-eu-west-1.amazonaws.com/2015_Developer_Scrape/5_products.html";
	private static final String SELECT_PRODUCTS = "#productLister ul li div.product";
	
	
	@Autowired
	private RestTemplate template;

	private ResponseEntity<String> download() {
		ResponseEntity<String> entity = template.getForEntity(DEFAULT_URL, String.class);
		return entity;
	}
	
	public List<Element> getElements() {
		String html = this.download().getBody();
		final Document htmlDoc = Jsoup.parse(html);
		List<Element> elements = htmlDoc.select(SELECT_PRODUCTS)
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
			Node n = new Node((Element)node);
			Product p = new Product(n.getName(), n.getSize(), n.getPrice(), n.getDescription());
				
			products.add(p);
		}		
		
		return products;
	}
	
	/**
	 * Build the json result bean
	 * @return
	 */
	public Result getResult() throws JsonProcessingException {
		Instant start = Instant.now();
    	List<Product> products = this.scrape();
    	Instant end = Instant.now();
    	
    	return new Result(products, Duration.between(start, end).toMillis()/1000.0);
	}
}
