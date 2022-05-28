/**
 * 
 */
package com.demo.ecommerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.demo.ecommerce.service.CsvOrderProcesser;
import com.demo.ecommerce.service.OrderService;
import com.demo.ecommerce.vm.PaginatedOrder;
import com.demo.ecommerce.vm.ResponseVm;

/**
 * @author User
 *
 */
@RestController
@RequestMapping("/orders")
public class OrderController {

	@Autowired
	private OrderService orderService; 

	@GetMapping("data")
	public String getData() {
		return "{ \"status\":\"success\"}";
	}

	@PostMapping("/upload")
	public ResponseEntity<ResponseVm> uploadFile(@RequestParam("file") MultipartFile file) {
		ResponseVm responseVm;
		if (CsvOrderProcesser.validCSVFormat(file)) {
			try {
			responseVm = orderService.uploadCsv(file);
			} catch (Exception e) {
				e.printStackTrace();
				responseVm = new ResponseVm(1,  "Fail to store csv data: ");
			}
		} else {
			responseVm = new ResponseVm(1,  "Please upload a csv file!");
		}
		return ResponseEntity.status(HttpStatus.OK).body(responseVm);
	}


	@PostMapping("/get")
	public PaginatedOrder getAll(@RequestBody PaginatedOrder paginatedOrder) {
		paginatedOrder.setOrders(orderService.getPaginatedOrders(paginatedOrder));
		return paginatedOrder;
	}

}
