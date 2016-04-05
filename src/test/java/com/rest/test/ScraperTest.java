package com.rest.test;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.scraping.application.Application;
import com.scraping.application.Product;
import com.scraping.application.Scraper;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@SpringApplicationConfiguration(classes = Application.class)
public class ScraperTest {

	@Autowired
	private Scraper scraper;
	
	/************************************************ Test Data *************************************************/
	private static final Product P1 = new Product("Sainsbury's Apricot Ripe & Ready x5", "38 KB", "3.50", "");
	private static final Product P2 = new Product("Sainsbury's Avocado Ripe & Ready XL Loose 300g", "38 KB", "1.50", "");
	private static final Product P3 = new Product("Sainsbury's Avocado, Ripe & Ready x2", "43 KB", "1.80", "");
	private static final Product P4 = new Product("Sainsbury's Avocados, Ripe & Ready x4", "38 KB", "3.20", "");
	private static final Product P5 = new Product("Sainsbury's Conference Pears, Ripe & Ready x4 (minimum)", "38 KB", "1.50", "");
	private static final Product P6 = new Product("Sainsbury's Golden Kiwi x4", "38 KB", "1.80", "");
	private static final Product P7 = new Product("Sainsbury's Kiwi Fruit, Ripe & Ready x4", "38 KB", "1.80", "");
	private static final List<Product> PLIST = Arrays.asList(P1, P2, P3, P4, P5, P6, P7);
	/*************************************************************************************************************/
	
	@Before
	public void setUp() {
		
	}

	@Test
	public void testProducts() throws Exception {
		
		List<Product> products = scraper.getResult().getResult();
		assertEquals(PLIST, products);
	}

}