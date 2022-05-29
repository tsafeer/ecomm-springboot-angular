/**
 * 
 */
package com.demo.ecommerce.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.demo.ecommerce.entity.Order;
import com.demo.ecommerce.repository.OrderRepository;
import com.demo.ecommerce.vm.PaginatedOrder;

/**
 * @author User
 *
 */
@ExtendWith(SpringExtension.class)
class OrderServiceTest {

	@Mock
	private OrderRepository orderRepository; 
	
	@InjectMocks
	private OrderService orderService;
	
	@Test
	void getPaginatedOrdersTest() {
		PaginatedOrder paginatedOrder = new PaginatedOrder();
		paginatedOrder.setPageIndex(0);
		paginatedOrder.setPageSize(5);
		Pageable firstPageWithTwoElements = PageRequest.of(paginatedOrder.getPageIndex(), paginatedOrder.getPageSize());
		doReturn(new PageImpl<Order>(Collections.emptyList())).when(orderRepository).findAll(firstPageWithTwoElements);
		when(orderRepository.count()).thenReturn(0L);
		assertEquals(0, orderService.getPaginatedOrders(paginatedOrder).size());
	}

}
