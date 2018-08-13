package com.agriniuk.imgaccorp.tempconvert;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;


public class TempService {
	
	
	/**
	 * Convert value to Farenheith (using {@link #convert(double, boolean)})
	 * 
	 * @param value temperature value (in Celsius) to convert to Farenheith
	 * @return converted value in Farenheith
	 */
	public static double toF(double value) {
		return convert(value, false);
	}
	
	
	/**
	 * Convert value to Celsius (using {@link #convert(double, boolean)})
	 * 
	 * @param value temperature value (in Farenheith) to convert to Celsius
	 * @return converted value in Celsius
	 */
	public static double toC(double value) {
		return convert(value, true);
	}
	
	
	/**
	 * An simple interface to w3schools' Celsius/Farenheit converter API.<br/>
	 * (<a href="https://www.w3schools.com/xml/tempconvert.asmx">https://www.w3schools.com/xml/tempconvert.asmx</a>)
	 * 
	 * @param value numerical value to convert 
	 * @param toCelsius to convert to Celsius or Farenheit
	 * @return converted value
	 * @throws RuntimeException if there are problems accessing API, or parsing response
	 */
	public static double convert(double value, boolean toCelsius) {
		String res = null;
		try {
			String body = (toCelsius ? "Fahrenheit=" : "Celsius=") + value;
			String url = "https://www.w3schools.com/xml/tempconvert.asmx/";
			url += toCelsius ? "FahrenheitToCelsius" : "CelsiusToFahrenheit";
			
			HttpClient client = HttpClientBuilder.create().build();
			HttpPost post = new HttpPost(url);
			post.setHeader("Cache-Control", "no-cache");
			post.setHeader("Content-Type", "application/x-www-form-urlencoded");
			
			post.setEntity(new ByteArrayEntity(body.getBytes("UTF-8")));

			HttpResponse response = client.execute(post);

			int status = response.getStatusLine().getStatusCode();
			if (status != 200)
				throw new RuntimeException("Status returned '" + status + "' is not 200");
			
			res = EntityUtils.toString(response.getEntity());
		} catch (Exception e) {
			throw new RuntimeException("Problem using w3schools API", e);
		}
		
		return Double.parseDouble(parse(res));
	}
	
	
	/**
	 * Parses an xml string and pulls its root node value<br/>
	 * 
	 * The xml string is the response of the w3schools API. It usually will look as:
	 * <pre>
	 * {@code
	 * <?xml version="1.0" encoding="utf-8"?>
	 * <string xmlns="https://www.w3schools.com/xml/">-12.1111111111111</string>
	 * }
	 * </pre>
	 * 
	 * @param xml xml string (as on example above)
	 * @return parsed value (in the example above it will be "-12.1111111111111")
	 * @throws RuntimeException if there are problems parsing xml
	 */
	public static String parse(String xml) {
		try {
			InputSource is = new InputSource(new StringReader(xml));
			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(is);
			String val = doc.getDocumentElement().getChildNodes().item(0).getNodeValue();
			return val;
		} catch (Exception e) {
			throw new RuntimeException("Error parsing xml", e);
		}
	}
	
}
