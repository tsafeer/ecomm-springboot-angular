/**
 * 
 */
package com.demo.ecommerce.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.demo.ecommerce.service.OrderService;
import com.demo.ecommerce.vm.PaginatedOrder;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author User
 *
 */
@WebMvcTest
@ExtendWith(SpringExtension.class)
class OrderControllerTest {

	@MockBean
	private OrderService orderService;
	
	@InjectMocks
	private OrderController orderController;
	
	@Autowired
	private MockMvc mockMvc;
	
	private ObjectMapper mapper = new ObjectMapper();

	@Test
	void uploadFileTest() throws Exception {
		
		MockMultipartFile firstFile = new MockMultipartFile("data", "filename.csv", "text/csv", "some xml".getBytes());
		when(orderService.uploadCsv(any())).thenReturn(null);
		
		this.mockMvc.perform(MockMvcRequestBuilders.multipart("/orders/upload")
				.file("file", firstFile.getBytes()))
				.andExpect(status().isOk());
	}

	
	
	@Test
	void orderListTest() throws Exception {
		
		PaginatedOrder paginatedOrder = new PaginatedOrder();
		when(orderService.getPaginatedOrders(paginatedOrder)).thenReturn(Collections.emptyList());
		
		this.mockMvc.perform(post("/orders/get")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(paginatedOrder)))
				.andExpect(status().isOk());
	}
}
