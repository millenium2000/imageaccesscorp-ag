package com.agriniuk.imgaccorp.tempconvert;

import org.junit.Test;
import static org.junit.Assert.*;


public class TempServiceTest {
	
	
	@Test
	public void test_convert() {
		String s;
		
		s = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n";
		s += "<string xmlns=\"https://www.w3schools.com/xml/\">-12.1111111111111</string>";
		assertEquals("-12.1111111111111", TempService.parse(s));
		
		
		s = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n";
		s += "<string>-10.1111111111111</string>";
		assertEquals("-10.1111111111111", TempService.parse(s));
	}

}
