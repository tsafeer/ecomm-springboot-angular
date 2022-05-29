/**
 * 
 */
package com.demo.ecommerce.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.web.multipart.MultipartFile;

import com.demo.ecommerce.entity.Country;
import com.demo.ecommerce.entity.ItemType;
import com.demo.ecommerce.entity.Order;
import com.demo.ecommerce.entity.Region;
import com.demo.ecommerce.entity.SalesChannel;
import com.demo.ecommerce.exception.BusinessException;
import com.demo.ecommerce.vm.Errors;

import lombok.extern.slf4j.Slf4j;

/**
 * @author User
 *
 */
@Slf4j
public class CsvOrderProcesser {
	
	private CsvOrderProcesser() {}

	private static final String TOTAL_PROFIT = "Total Profit";
	private static final String TOTAL_COST = "Total Cost";
	private static final String TOTAL_REVENUE = "Total Revenue";
	private static final String UNIT_COST = "Unit Cost";
	private static final String UNIT_PRICE = "Unit Price";
	private static final String UNITS_SOLD = "Units Sold";
	private static final String SHIP_DATE = "Ship Date";
	private static final String ORDER_ID = "Order ID";
	private static final String ORDER_DATE = "Order Date";
	private static final String ORDER_PRIORITY = "Order Priority";
	private static final String SALES_CHANNEL = "Sales Channel";
	private static final String ITEM_TYPE = "Item Type";
	private static final String COUNTRY = "Country";
	private static final String REGION = "Region";
	private static final String NOT_A_VALID_DATE = "Not a valid Date : ";
	private static final String NOT_A_VALID_NUMBER = "Not a valid number : ";
	
	public static final String TYPE = "text/csv";
	protected static final String[] CSV_HEADERS = {REGION,COUNTRY,ITEM_TYPE,SALES_CHANNEL,ORDER_PRIORITY,ORDER_DATE,ORDER_ID,SHIP_DATE,UNITS_SOLD,UNIT_PRICE,UNIT_COST,TOTAL_REVENUE,TOTAL_COST,TOTAL_PROFIT};

	/**
	 * @param file
	 * @return
	 */
	public static boolean validCSVFormat(MultipartFile file) {
		return TYPE.equals(file.getContentType());
	}
	
	/**
	 * @param file
	 * @return
	 */
	public static boolean isAllowedSize(MultipartFile file) {
		log.debug("file size : {}", file.getSize());
		return file.getSize() < 68157440;
	}

	/**
	 * @param is
	 * @param masterData
	 * @param errors
	 * @return
	 * @throws Exception
	 */
	public static List<Order> parseCsv(InputStream is, final CsvOrderProcesserVm masterData, final Map<String, Errors> errors) throws BusinessException {
		
		try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
				CSVParser csvParser = new CSVParser(fileReader,
						CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());) {
			final List<Order> orders = new ArrayList<>();
			List<CSVRecord> csvRecords = csvParser.getRecords();
			/**Partition<CSVRecord> ofSize = Partition.ofSize(csvRecords, 1000);
			ofSize.parallelStream().forEach(chunks -> { chunks.forEach(					
					csvRecord -> {
						try {
							Order ord = parseOrderDetail(errors, masterData, csvRecord);
							log.debug("order : {}", ord);
							if(ord != null) {
								orders.add(ord);
							}
						} catch (NumberFormatException | ParseException e) {
							e.printStackTrace();
						}


					});
			});*/
			for (CSVRecord csvRecord : csvRecords) {
				processOrder(masterData, errors, orders, csvRecord);

			}

			return orders;
		} catch (IOException e) {
			throw new BusinessException("Fail to parse CSV file: " + e.getMessage());
		}
	}

	/**
	 * @param masterData
	 * @param errors
	 * @param orders
	 * @param csvRecord
	 */
	private static void processOrder(final CsvOrderProcesserVm masterData, final Map<String, Errors> errors,
			final List<Order> orders, CSVRecord csvRecord) {
		try {
			Order ord = parseOrderDetail(errors, masterData, csvRecord);
			if(log.isTraceEnabled()) {
				log.trace("order : {}", ord);
			}
			if(ord != null) {
				orders.add(ord);
			}
		} catch (NumberFormatException e) {
			log.error(e.getMessage(), e);
		}
	}
	
	/**
	 * @param errors
	 * @param masterData
	 * @param csvRecord
	 * @return
	 * @throws NumberFormatException
	 */
	private static Order parseOrderDetail(Map<String, Errors> errors, CsvOrderProcesserVm masterData, CSVRecord csvRecord) throws NumberFormatException {
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		sdf.setLenient(false);
		
		long lineNo = csvRecord.getRecordNumber();
		Order order = null;
		boolean hasError = false;
		hasError = validateStringValues(csvRecord, errors, lineNo);

		hasError = hasError || validateDateValues(csvRecord, errors, lineNo, sdf);

		hasError = hasError || validateNumberValues(csvRecord, errors, lineNo);

		order = populateOrder(masterData, sdf, lineNo, order, hasError, csvRecord);
		return order;
	}

	/**
	 * @param csvRecord
	 * @param errors
	 * @param lineNo
	 * @return
	 */
	private static boolean validateNumberValues(CSVRecord csvRecord, Map<String, Errors> errors, long lineNo) {
		boolean hasError = false;
		String orderId = csvRecord.get(ORDER_ID);
		hasError = addErrorsIfEmpty(orderId, errors, lineNo, ORDER_ID);
		hasError = hasError || addErrorsIf(!isLong(orderId), errors, lineNo, ORDER_ID, NOT_A_VALID_NUMBER + orderId);
		

		String unitsSold = csvRecord.get(UNITS_SOLD);
		hasError = hasError || addErrorsIfEmpty(unitsSold, errors, lineNo, UNITS_SOLD);
		hasError = hasError || addErrorsIf(!isLong(unitsSold), errors, lineNo, UNITS_SOLD, NOT_A_VALID_NUMBER + unitsSold);

		String unitPrice = csvRecord.get(UNIT_PRICE);
		hasError = hasError || addErrorsIfEmpty(unitPrice, errors, lineNo, UNIT_PRICE);
		hasError = hasError || addErrorsIf(!isDouble(unitPrice), errors, lineNo, UNIT_PRICE, NOT_A_VALID_NUMBER + unitPrice);

		String unitCost = csvRecord.get(UNIT_COST);
		hasError = hasError || addErrorsIfEmpty(unitCost, errors, lineNo, UNIT_COST);
		hasError = hasError || addErrorsIf(!isDouble(unitCost), errors, lineNo, UNIT_COST, NOT_A_VALID_NUMBER + unitCost);

		String totalRevenue = csvRecord.get(TOTAL_REVENUE);
		hasError = hasError || addErrorsIfEmpty(totalRevenue, errors, lineNo, TOTAL_REVENUE);
		hasError = hasError || addErrorsIf(!isDouble(totalRevenue), errors, lineNo, TOTAL_REVENUE, NOT_A_VALID_NUMBER + totalRevenue);

		String totalCost = csvRecord.get(TOTAL_COST);
		hasError = hasError || addErrorsIfEmpty(totalCost, errors, lineNo, TOTAL_COST);
		hasError = hasError || addErrorsIf(!isDouble(totalCost), errors, lineNo, TOTAL_COST, NOT_A_VALID_NUMBER + totalCost);

		String totalProfit = csvRecord.get(TOTAL_PROFIT);
		hasError = hasError || addErrorsIfEmpty(totalProfit, errors, lineNo, TOTAL_PROFIT);
		hasError = hasError || addErrorsIf(!isDouble(totalProfit), errors, lineNo, TOTAL_PROFIT, NOT_A_VALID_NUMBER + totalProfit);
		return hasError;
	}

	/**
	 * @param csvRecord
	 * @param errors
	 * @param lineNo
	 * @param sdf
	 * @return
	 */
	private static boolean validateDateValues(CSVRecord csvRecord, Map<String, Errors> errors, long lineNo, SimpleDateFormat sdf) {
		boolean hasError = false;
		String shipDate = csvRecord.get(SHIP_DATE);
		hasError = addErrorsIfEmpty(shipDate, errors, lineNo, SHIP_DATE);
		hasError = hasError || addErrorsIf(!validDate(sdf, shipDate), errors, lineNo, SHIP_DATE, NOT_A_VALID_DATE + shipDate);
		
		String orderDate = csvRecord.get(ORDER_DATE);
		hasError = hasError || addErrorsIfEmpty(orderDate, errors, lineNo, ORDER_DATE);
		hasError = hasError || addErrorsIf(!validDate(sdf, orderDate), errors, lineNo, ORDER_DATE, NOT_A_VALID_DATE + orderDate);
		return hasError;
	}

	/**
	 * @param csvRecord
	 * @param errors
	 * @param lineNo
	 * @return
	 */
	private static boolean validateStringValues(CSVRecord csvRecord, Map<String, Errors> errors, long lineNo) {
		boolean hasError = false;
		hasError = addErrorsIfEmpty(csvRecord.get(REGION), errors, lineNo, REGION);

		hasError = hasError || addErrorsIfEmpty(csvRecord.get(COUNTRY), errors, lineNo, COUNTRY);

		hasError = hasError || addErrorsIfEmpty(csvRecord.get(ITEM_TYPE), errors, lineNo, ITEM_TYPE);

		hasError = hasError || addErrorsIfEmpty(csvRecord.get(SALES_CHANNEL), errors, lineNo, SALES_CHANNEL);

		hasError = hasError || addErrorsIfEmpty(csvRecord.get(ORDER_PRIORITY), errors, lineNo, ORDER_PRIORITY);
		
		return hasError;
	}

	/**
	 * @param masterData
	 * @param sdf
	 * @param lineNo
	 * @param order
	 * @param hasError
	 * @param region
	 * @param country
	 * @param itemType
	 * @param salesChannel
	 * @param orderPriority
	 * @param orderDate
	 * @param orderId
	 * @param shipDate
	 * @param unitsSold
	 * @param unitPrice
	 * @param unitCost
	 * @param totalRevenue
	 * @param totalCost
	 * @param totalProfit
	 * @param csvRecord 
	 * @return
	 */
	private static Order populateOrder(CsvOrderProcesserVm masterData, SimpleDateFormat sdf, long lineNo, Order order,
			boolean hasError, CSVRecord csvRecord) {
		if(!hasError) {
			try {
			order =  new Order()
					.setOrderId(Long.parseLong(csvRecord.get(ORDER_ID)))
					.setOrderedBy(getNric())
					.setOrderDate(sdf.parse(csvRecord.get(ORDER_DATE)))
					.setOrderPriority(csvRecord.get(ORDER_PRIORITY))
					.setCountry(getCountry(masterData, csvRecord.get(COUNTRY)))
					.setItemType(getItemType(masterData, csvRecord.get(ITEM_TYPE)))
					.setRegion(getRegion(masterData, csvRecord.get(REGION)))
					.setSalesChannel(getSalesChannel(masterData, csvRecord.get(SALES_CHANNEL)))
					.setShipDate(sdf.parse(csvRecord.get(SHIP_DATE)))
					.setTotalCost(Double.parseDouble(csvRecord.get(TOTAL_COST)))
					.setTotalProfit(Double.parseDouble(csvRecord.get(TOTAL_PROFIT)))
					.setTotalRevenue(Double.parseDouble(csvRecord.get(TOTAL_REVENUE)))
					.setUnitCost(Double.parseDouble(csvRecord.get(UNIT_COST)))
					.setUnitPrice(Double.parseDouble(csvRecord.get(UNIT_PRICE)))
					.setUnitsSold(Long.parseLong(csvRecord.get(UNITS_SOLD)))
					.setNew(true);
			} catch (Exception e) {
				log.error("Error in line : " + lineNo + " : error : " + e.getMessage());
			}
		}
		return order;
	}

	/**
	 * @param masterData
	 * @param key
	 * @return
	 */
	private static SalesChannel getSalesChannel(CsvOrderProcesserVm masterData, String key) {
		if(masterData.salesChannelMap.containsKey(key))
			return masterData.salesChannelMap.get(key);
		SalesChannel x = new SalesChannel(key);
		masterData.salesChannelMap.put(key, x);
		return x;
	}

	/**
	 * @param masterData
	 * @param key
	 * @return
	 */
	private static Region getRegion(CsvOrderProcesserVm masterData, String key) {
		if(masterData.regionMap.containsKey(key))
			return masterData.regionMap.get(key);
		Region x = new Region(key);
		masterData.regionMap.put(key, x);
		return x;
	}

	/**
	 * @param masterData
	 * @param key
	 * @return
	 */
	private static ItemType getItemType(CsvOrderProcesserVm masterData, String key) {
		if(masterData.itemTypeMap.containsKey(key))
			return masterData.itemTypeMap.get(key);
		ItemType x = new ItemType(key);
		masterData.itemTypeMap.put(key, x);
		return x;
	}

	/**
	 * @param masterData
	 * @param key
	 * @return
	 */
	private static Country getCountry(CsvOrderProcesserVm masterData, String key) {
		if(masterData.countryMap.containsKey(key))
			return masterData.countryMap.get(key);
		Country x = new Country(key);
		masterData.countryMap.put(key, x);
		return x;
	}

	/**
	 * @param val
	 * @return
	 */
	private static boolean isDouble(String val) {
		try {
			Double.parseDouble(val);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * @param val
	 * @return
	 */
	private static boolean isLong(String val) {
		try {
			Long.parseLong(val);
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	/**
	 * @param val
	 * @return
	 */
	private static boolean validDate(SimpleDateFormat sdf, String val) {
		try {
			sdf.parse(val);
		}catch (Exception e) {
			return false;
		}
		return true;
	}

	
	/**
	 * @return
	 */
	private static String getNric() {
		Random random = new Random();
		String[] starting = {"S","T","F","G"};
		int index = getRandomNumberUsingNextInt(random,0,3);
		int val = getRandomNumberUsingNextInt(random, 1111111, 9999999);
		char c = (char)(random.nextInt(26) + 'a');
		String nric = starting[index] + val + c;
		
		return nric.toUpperCase();
	}
	
	/**
	 * @param min
	 * @param max
	 * @param random 
	 * @return
	 */
	public static int getRandomNumberUsingNextInt(Random random, int min, int max) {
	    
	    return random.nextInt(max - min) + min;
	}

	/**
	 * @param string
	 * @param errors
	 * @param lineNo
	 * @param fieldName
	 * @return
	 */
	private static boolean addErrorsIfEmpty(String string, Map<String, Errors> errors, long lineNo, String fieldName) {
		String message = fieldName + " is mandatory";
		return addErrorsIf((string == null || string.trim().length() == 0), errors, lineNo, fieldName, message);

	}

	/**
	 * @param b
	 * @param errors
	 * @param lineNo
	 * @param fieldName
	 * @param message
	 * @return
	 */
	private static boolean addErrorsIf(boolean b, Map<String, Errors> errors, long lineNo, String fieldName, String message) {
		if(b) {
			addErrors(errors, lineNo, fieldName, message);
		}
		return b;
	}

	/**
	 * @param errors
	 * @param lineNo
	 * @param fieldName
	 * @param message
	 */
	private static void addErrors(Map<String, Errors> errors, long lineNo, String fieldName, String message) {
		String key = ""+lineNo+"-"+fieldName;
		if(errors.containsKey(key)) {
			errors.get(key).addMessage(message);
		} else {
			errors.put(key, new Errors(lineNo, fieldName, message));
		}
	}
}
