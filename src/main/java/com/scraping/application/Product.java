package com.scraping.application;

import java.util.Objects;

/**
 *	Product bean 
 *
 */
public class Product {

	private String title;
	private String size;
	private String unit_price;
	private String description;
	
	public Product(String title, String size, String unit_price, String description) {
		super();
		this.title = title;
		this.size = size;
		this.unit_price = unit_price;
		this.description = description;
	}

	public String getTitle() {
		return title;
	}

	public String getSize() {
		return size;
	}

	public String getUnit_price() {
		return unit_price;
	}

	public String getDescription() {
		return description;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Product) {
			Product other = (Product) obj;
			
			return	Objects.equals(this.title, other.title) &&
					Objects.equals(this.size, other.size) &&
					Objects.equals(this.unit_price, other.unit_price) &&
					Objects.equals(this.description, other.description);
		}

		return false;
	}

	@Override
	public String toString() {
		return "Product [title=" + title + ", size=" + size + ", unit_price=" + unit_price + ", description="+ description + "]";
	}

}