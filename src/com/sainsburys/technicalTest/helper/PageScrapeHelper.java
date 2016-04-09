package com.sainsburys.technicalTest.helper;

import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.sainsburys.technicalTest.constants.TechnicalTestConstants;
import com.sainsburys.technicalTest.controller.ScrapeController;

/**
 * Helper for constructing Json string.
 * 
 * @author Manoj.Dhore
 *
 */
public class PageScrapeHelper {
	
	private final static Logger log = Logger.getLogger(ScrapeController.class);
	
	private static BigDecimal totalProductPrice = new BigDecimal("0.00");
	
	public PageScrapeHelper(){}

	/**
	 * Retrieves the file size for product details page.
	 * 
	 * @param url
	 * @return content length if available other wise 'Not available' string
	 */
	public String getFileSize(String url) {
		
		String fileSize = null;
   	 	HttpURLConnection conn = null;
	   	try {
	   		   URL obj = new URL(url);
	           conn = (HttpURLConnection)obj.openConnection();
	           conn.setRequestMethod(TechnicalTestConstants.HEAD);
	           Map<String, List<String>> map = conn.getHeaderFields();
	           List<String> contentLength = map.get(TechnicalTestConstants.CONTENT_LENGTH);
	
	           if (contentLength == null) {
	               fileSize =  TechnicalTestConstants.NOT_AVAILABLE;
	           } else {
	               fileSize = String.valueOf(Double.parseDouble(contentLength.get(0))/1000) + TechnicalTestConstants.KB;
	           }
	       } catch (Exception e) {
	    	   log.error("Error retriving size : " + e.getMessage());
	       }
	       return fileSize;
   }
	
	public String getUnitPrice(String priceString){
		String productPrice = priceString.subSequence(6, 10).toString();
		calculateTotalProductPrice(productPrice);
		return productPrice;
	}
	
	/**
	 * Add the individual product price.
	 * 
	 * @param productPrice
	 */
	private void calculateTotalProductPrice(String productPrice){
		 if(productPrice != null){
			 totalProductPrice = totalProductPrice.add(BigDecimal.valueOf(Double.parseDouble(productPrice)));
		 }
	}

	public BigDecimal getTotalProductPrice() {
		return totalProductPrice;
	}


}
