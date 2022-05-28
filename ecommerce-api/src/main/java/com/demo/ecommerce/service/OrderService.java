/**
 * 
 */
package com.demo.ecommerce.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.demo.ecommerce.entity.Country;
import com.demo.ecommerce.entity.ItemType;
import com.demo.ecommerce.entity.Order;
import com.demo.ecommerce.entity.Region;
import com.demo.ecommerce.entity.SalesChannel;
import com.demo.ecommerce.repository.CountryRepository;
import com.demo.ecommerce.repository.ItemTypeRepository;
import com.demo.ecommerce.repository.OrderRepository;
import com.demo.ecommerce.repository.RegionRepository;
import com.demo.ecommerce.repository.SalesChannelRepository;
import com.demo.ecommerce.util.Partition;
import com.demo.ecommerce.vm.Errors;
import com.demo.ecommerce.vm.OrderVm;
import com.demo.ecommerce.vm.PaginatedOrder;
import com.demo.ecommerce.vm.ResponseVm;

import lombok.extern.slf4j.Slf4j;

/**
 * @author User
 *
 */
@Service
@Slf4j
public class OrderService {

	@Autowired
	private OrderRepository orderRepository; 
	
	@Autowired
	private CountryRepository countryRepository; 
	
	@Autowired
	private ItemTypeRepository itemTypeRepository; 
	
	@Autowired
	private RegionRepository regionRepository; 
	
	@Autowired
	private SalesChannelRepository salesChannelRepository; 
	
	public List<Order> getAllOrders(){
		Pageable firstPageWithTwoElements = PageRequest.of(0,0);
		return orderRepository.findAll(firstPageWithTwoElements).toList();
	}

	@Transactional
	public ResponseVm uploadCsv(MultipartFile file) throws Exception {

		String message = "";
		int status = 0; 
		List<Errors> errorList = null;
		Map<String, Errors> errors = new HashMap<>();
		CsvOrderProcesserVm masterData = getMasterData();
		//Porcess the file
		List<Order> orders = CsvOrderProcesser.parseCsv(file.getInputStream(), masterData, errors);

		//Check and persist
		if(!errors.isEmpty()) {
			log.error("validation failed : {}", errors );
			status = 1;

			message = "Could not upload the file: " + file.getOriginalFilename() + "!";
			errorList = errors.entrySet().stream().map(i -> i.getValue()).collect(Collectors.toList());
		}else {
			message = "Uploaded the file successfully: " + file.getOriginalFilename();
			regionRepository.saveAll(masterData.regionMap.values().stream().collect(Collectors.toList()));
			countryRepository.saveAll(masterData.countryMap.values().stream().collect(Collectors.toList()));
			itemTypeRepository.saveAll(masterData.itemTypeMap.values().stream().collect(Collectors.toList()));
			salesChannelRepository.saveAll(masterData.salesChannelMap.values().stream().collect(Collectors.toList()));
			Partition<Order> ofSize = Partition.ofSize(orders, 1000);
			ofSize.parallelStream().forEach(i -> orderRepository.saveAll(i));

		} 
		ResponseVm response = new ResponseVm(status, message, errorList);
		return response;
	}

	private CsvOrderProcesserVm getMasterData() {
		Map<String,Country> countryMap;
		List<Country> allCountry = countryRepository.findAll();
		if(allCountry != null && !allCountry.isEmpty()) {
			countryMap = allCountry.stream().collect(Collectors.toMap(i -> i.getCountryName(), i -> i));
		}else {
			countryMap = new HashMap<>();
		}
		
		Map<String,Region> regionMap;
		List<Region> allRegion = regionRepository.findAll();
		if(allRegion != null && !allRegion.isEmpty()) {
			regionMap = allRegion.stream().collect(Collectors.toMap(i -> i.getRegionName(), i -> i));
		}else {
			regionMap = new HashMap<>();
		}
		
		Map<String,ItemType> itemTypeMap;
		List<ItemType> allItemType = itemTypeRepository.findAll();
		if(allItemType != null && !allItemType.isEmpty()) {
			itemTypeMap = allItemType.stream().collect(Collectors.toMap(i -> i.getItemTypeName(), i -> i));
		}else {
			itemTypeMap = new HashMap<>();
		}
		
		Map<String,SalesChannel> salesChannelMap;
		List<SalesChannel> allSalesChannel = salesChannelRepository.findAll();
		if(allSalesChannel != null && !allSalesChannel.isEmpty()) {
			salesChannelMap = allSalesChannel.stream().collect(Collectors.toMap(i -> i.getSalesChannelName(), i -> i));
		}else {
			salesChannelMap = new HashMap<>();
		}
		CsvOrderProcesserVm vm = new CsvOrderProcesserVm();
		vm.setCountryMap(countryMap)
			.setItemTypeMap(itemTypeMap)
			.setRegionMap(regionMap)
			.setSalesChannelMap(salesChannelMap);
		
		return vm;
		
		
	}


	public List<OrderVm> getPaginatedOrders(PaginatedOrder paginatedOrder) {
		Pageable firstPageWithTwoElements = PageRequest.of(paginatedOrder.getPageIndex(), paginatedOrder.getPageSize());
		long count = orderRepository.count();
		paginatedOrder.setLength(count);
		return populateOrderVm(orderRepository.findAll(firstPageWithTwoElements).toList());
	}

	private List<OrderVm> populateOrderVm(List<Order> orders) {
		if(orders != null && !orders.isEmpty()) {
			return orders.stream().map(Order::getOrderVm).collect(Collectors.toList());
		}
		return Collections.emptyList();
	}

}
