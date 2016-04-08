package com.scraping.application;

import java.util.List;

/*
 * Bean result
 */
public class Result {

	final private List<Product> result; /* products */
	final private double time; /* execution time */
	
	
	public Result(List<Product> result, double time) {
		super();
		this.result = result;
		this.time = time;
	}

	public List<Product> getResult() {
		return result;
	}

	public double getTime() {
		return time;
	}
	
}
