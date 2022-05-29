/**
 * 
 */
package com.demo.ecommerce.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * @author User
 *
 */
class PartitionTest {

	@Test
	void ofSizeTest() {
		List<String> list = Arrays.asList(new String[]{"A","A","A","A","A","A","A","A","A","A","A","A","A","A","A","A","A","A"});
		Partition<String> fullList = Partition.ofSize(list, 3);
		assertEquals(6, fullList.size());
	}

}
