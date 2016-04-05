package com.scraping.application;

import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

/**
 * Represent a node of the html product
 *
 */
public class Node {
	private static final String SELECT_NAME = "h3 a";
	private static final String SELECT_LINK = "h3 a";
	private static final String SELECT_PRICE = "p.pricePerUnit";
	private static final String CONTENT_LENGTH = "Content-Length";
	
	private Element e;
	
	
	public Node(Element e) {
		this.e = e;
	}

	
	public String getName() {
		return e.select(SELECT_NAME).text();
	}
	
	public String getLink() {
		return e.select(SELECT_LINK).first().attr("abs:href");
	}
	
	public String getSize() {
		String link = getLink();
		if (link != "") {
			try {
				int bytes = Integer.parseInt(Jsoup.connect(link).execute().header(CONTENT_LENGTH)); /* retrieve page link size in bytes */
				return FileUtils.byteCountToDisplaySize(bytes);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return "";
	}

	public String getPrice() {
		return e.select(SELECT_PRICE).text().replaceAll("[^0-9.,]+",""); /* keeps only the price */
	}


	public String getDescription() {
		return ""; // no description in html
	}
	
}
