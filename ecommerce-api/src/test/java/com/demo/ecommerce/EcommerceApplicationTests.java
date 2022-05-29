package com.demo.ecommerce;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.demo.ecommerce.controller.OrderController;
import com.demo.ecommerce.service.OrderService;

/**
 * @author User
 *
 */
@SpringBootTest
class EcommerceApplicationTests {

	@Autowired
    private OrderController orderController;

    @Autowired
    private OrderService orderService;

    @Test
    void contextLoads() throws Exception {
        assertThat(orderController).isNotNull();
        assertThat(orderService).isNotNull();
    }
	

}
