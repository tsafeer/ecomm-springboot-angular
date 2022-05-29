/**
 * 
 */
package com.demo.ecommerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

import lombok.extern.slf4j.Slf4j;

/**
 * @author User
 *
 */
@Slf4j
@RestController
@RequestMapping("/orders")
public class OrderController {

	@Autowired
	private OrderService orderService; 

	/**
	 * @param file
	 * @return
	 */
	@PostMapping("/upload")
	public ResponseEntity<ResponseVm> uploadFile(@RequestParam("file") MultipartFile file) {
		ResponseVm responseVm;

		if(!CsvOrderProcesser.isAllowedSize(file)) {
			responseVm = new ResponseVm(1,  "Please upload a csv file less than 65MB!");
		} else if (!CsvOrderProcesser.validCSVFormat(file)) {
			responseVm = new ResponseVm(1,  "Please upload a csv file!");
		} else {
			try {
				responseVm = orderService.uploadCsv(file);
			} catch(DataIntegrityViolationException e) {
				responseVm = new ResponseVm(1,  "Order already exists or duplicate orders found");
			}catch (Exception e) {
				e.printStackTrace();
				responseVm = new ResponseVm(1,  "Fail to store csv data: ");
			}
		}
		log.info("Upload completed with response : {}" , responseVm);
		return ResponseEntity.status(HttpStatus.OK).body(responseVm);
	}


	/**
	 * @param paginatedOrder
	 * @return
	 */
	@PostMapping("/get")
	public PaginatedOrder getAll(@RequestBody PaginatedOrder paginatedOrder) {
		paginatedOrder.setOrders(orderService.getPaginatedOrders(paginatedOrder));
		return paginatedOrder;
	}

}
