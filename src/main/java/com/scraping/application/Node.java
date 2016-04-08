package com.scraping.application;

import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

public class Node {
	
	private static final String CONTENT_LENGTH = "Content-Length";

	private ScraperSettings config;
	
	private Element e;
	
	
	public Node(Element e, ScraperSettings config) {
		this.e = e;
		this.config = config;
	}
	
	/**
	 * Get product name in html node
	 * @return name
	 */
	public String getName() {
		return e.select(config.getTitle()).text();
	}
	
	/**
	 * Get link in html node
	 * @return link
	 */
	public String getLink() {
		return e.select(config.getLink()).first().attr("abs:href");
	}

	/**
	 * Get size in bytes of link in html node
	 * @return size
	 */
	public String getSize() {
		String link = getLink();
		
		if (link != "") {
			try {
				int bytes = Integer.parseInt(Jsoup.connect(link).execute().header(CONTENT_LENGTH)); 
				return FileUtils.byteCountToDisplaySize(bytes);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return "";
	}

	/**
	 * Get price from html node
	 * @return price
	 */
	public String getPrice() {
		return e.select(config.getPrice())
					.text()
					.replaceAll("[^0-9.,]+",""); /* keeps only the price */
	}


	public String getDescription() {
		return ""; // no description in html
	}
	
}
