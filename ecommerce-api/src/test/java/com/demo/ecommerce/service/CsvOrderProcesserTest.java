/**
 * 
 */
package com.demo.ecommerce.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

/**
 * @author User
 *
 */
class CsvOrderProcesserTest {

	@Test
	void validCSVFormatTest() {
		MockMultipartFile firstFile = new MockMultipartFile("data", "filename.csv", "text/csv", "some xml".getBytes());
		boolean allowedSize = CsvOrderProcesser.validCSVFormat(firstFile);
		assertTrue(allowedSize);
	}
	
	@Test
	void isAllowedSizeTest() {
		MockMultipartFile firstFile = new MockMultipartFile("data", "filename.csv", "text/csv", "some xml".getBytes());
		boolean allowedSize = CsvOrderProcesser.isAllowedSize(firstFile);
		assertTrue(allowedSize);
	}

}
