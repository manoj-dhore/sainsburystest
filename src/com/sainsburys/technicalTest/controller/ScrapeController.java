package com.sainsburys.technicalTest.controller;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.sainsburys.technicalTest.constants.TechnicalTestConstants;
import com.sainsburys.technicalTest.helper.JsonBuilder;

/**
 * This is the entry point for console application.
 * 
 * @author Manoj.Dhore
 *
 */
public class ScrapeController {

	private final static Logger log = Logger.getLogger(ScrapeController.class);
	
	/**
	 * @param args
	 * @throws JSONException 
	 */
	public static void main(String[] args) {
		
		try {
			String url = TechnicalTestConstants.PRODUCT_LIST_URL;
			
			//Document for product list page.
			Document doc = Jsoup.connect(url).get();
		
			//Parse the document and retrieve element with class: 'product' 
			Elements productContentsElements = doc.getElementsByClass(TechnicalTestConstants.PRODUCT);
			
			JsonBuilder jsonBuilder = new JsonBuilder();
			
			log.info(jsonBuilder.constructJson(productContentsElements));
			
			
		} catch (IOException e) {
			log.error(e.getMessage());
		} catch (JSONException e) {
			log.error(e.getMessage());
		}
    }
}
