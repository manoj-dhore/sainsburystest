package com.sainsburys.technicalTest.helper;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.select.NodeTraversor;

import com.sainsburys.technicalTest.constants.TechnicalTestConstants;
import com.sainsburys.technicalTest.controller.ScrapeController;

/**
 * Constructs the JSON object parsing the 
 * 
 * @author Manoj.Dhore
 *
 */
public class JsonBuilder {

	private final static Logger log = Logger.getLogger(JsonBuilder.class);
	
	public JsonBuilder(){}
	
	
	public String constructJson(Elements childElements) throws JSONException, IOException{
		
		PageScrapeHelper pageScrapeHelper = new PageScrapeHelper();
		log.info("Started Constructing JSON");
		final JSONObject result = new JSONObject();
        for(Element childElement: childElements){
        	
        	FormattingVisitor formatter = new FormattingVisitor();
            NodeTraversor traversor = new NodeTraversor(formatter);
            
            //Traverse the childelement. 
            //Elements are returned in following sequence : 1.PRoduct title 2.Link to product details page 3. Unit price for product.
            traversor.traverse(childElement);
            
            final JSONObject productInfoJson = new JSONObject();
            
            //Tokenize the list in sequence
            StringTokenizer tokenizer = new StringTokenizer(formatter.toString(), "\n");
            
            //Firts element is product title
            productInfoJson.put(TechnicalTestConstants.TITLE, tokenizer.nextToken().trim());
            
            //Link to product details page.
            String productLink = tokenizer.nextToken().trim();
            //Request for product details page and get the content size. 
            productInfoJson.put(TechnicalTestConstants.SIZE, pageScrapeHelper.getFileSize(productLink));
            
            //Connect to the product details page from the link retrieve above and get the document
            Document productDoc = Jsoup.connect(productLink).get();
            
            //Parse the document and search for itemprop attribute which hold the product description
            Elements productElements = productDoc.getElementsByAttributeValue(TechnicalTestConstants.ITEM_PROP, TechnicalTestConstants.DESCRIPTION);
            productInfoJson.put(TechnicalTestConstants.DESCRIPTION, productElements.first().text());
           
            //Unit price for the product
            String productPrice = tokenizer.nextToken().trim();
            productInfoJson.put(TechnicalTestConstants.UNIT_PRICE, pageScrapeHelper.getUnitPrice(productPrice));
            
            result.append(TechnicalTestConstants.RESULT, productInfoJson);
            
        }
        final JSONObject products = new JSONObject(result.toString());
        products.put(TechnicalTestConstants.TOTAL, pageScrapeHelper.getTotalProductPrice());
        
        return products.toString();
	}
	
}
