/**
 * 
 */
package com.demo.ecommerce.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
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
import com.demo.ecommerce.vm.Errors;

import lombok.extern.slf4j.Slf4j;

/**
 * @author User
 *
 */
@Slf4j
public class CsvOrderProcesser {

	private static SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

	public static String TYPE = "text/csv";
	static String[] HEADERs = {"Region","Country","Item Type","Sales Channel","Order Priority","Order Date","Order ID","Ship Date","Units Sold","Unit Price","Unit Cost","Total Revenue","Total Cost","Total Profit"};

	public static boolean validCSVFormat(MultipartFile file) {
		if (!TYPE.equals(file.getContentType())) {
			return false;
		}
		return true;
	}

	public static List<Order> parseCsv(InputStream is, final CsvOrderProcesserVm masterData, final Map<String, Errors> errors) throws Exception {
		try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
				CSVParser csvParser = new CSVParser(fileReader,
						CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());) {
			final List<Order> orders = new ArrayList<>();
			List<CSVRecord> csvRecords = csvParser.getRecords();
			/*Partition<CSVRecord> ofSize = Partition.ofSize(csvRecords, 1000);
			ofSize.parallelStream().forEach(chunks -> { chunks.forEach(					
					csvRecord -> {
						try {
							Order ord = parseOrderDetail(errors, masterData, csvRecord);
							log.debug("order : {}", ord);
							if(ord != null) {
								orders.add(ord);
							}
						} catch (NumberFormatException | ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}


					});
			});*/
			for (CSVRecord csvRecord : csvRecords) {
				try {
					Order ord = parseOrderDetail(errors, masterData, csvRecord);
					log.debug("order : {}", ord);
					if(ord != null) {
						orders.add(ord);
					}
				} catch (NumberFormatException | ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

			return orders;
		} catch (IOException e) {
			throw new RuntimeException("fail to parse CSV file: " + e.getMessage());
		}
	}

	private static Order parseOrderDetail(Map<String, Errors> errors, CsvOrderProcesserVm masterData, CSVRecord csvRecord) throws NumberFormatException, ParseException {
		long lineNo = csvRecord.getRecordNumber();
		Order order = null;
		//System.out.println("Processing line no: " + lineNo);
		boolean hasError = false;
		String region = csvRecord.get("Region");
		hasError = addErrorsIfEmpty(region, errors, lineNo, "Region");

		String country = csvRecord.get("Country");
		hasError = hasError || addErrorsIfEmpty(country, errors, lineNo, "Country");

		String itemType = csvRecord.get("Item Type");
		hasError = hasError || addErrorsIfEmpty(itemType, errors, lineNo, "Item Type");

		String salesChannel = csvRecord.get("Sales Channel");
		hasError = hasError || addErrorsIfEmpty(salesChannel, errors, lineNo, "Sales Channel");

		String orderPriority = csvRecord.get("Order Priority");
		hasError = hasError || addErrorsIfEmpty(salesChannel, errors, lineNo, "Order Priority");

		String orderDate = csvRecord.get("Order Date");
		hasError = hasError || addErrorsIfEmpty(orderDate, errors, lineNo, "Order Date");
		hasError = hasError || addErrorsIf(!validDate(orderDate), errors, lineNo, "Order Date", "Invalid Date format : " + orderDate);

		String orderId = csvRecord.get("Order ID");
		hasError = hasError || addErrorsIfEmpty(orderId, errors, lineNo, "Order ID");
		hasError = hasError || addErrorsIf(!isLong(orderId), errors, lineNo, "Order ID", "Not a valid number : " + orderId);

		String shipDate = csvRecord.get("Ship Date");
		hasError = hasError || addErrorsIfEmpty(shipDate, errors, lineNo, "Ship Date");
		hasError = hasError || addErrorsIf(!validDate(shipDate), errors, lineNo, "Ship Date", "Invalid Date format : " + shipDate);

		String unitsSold = csvRecord.get("Units Sold");
		hasError = hasError || addErrorsIfEmpty(unitsSold, errors, lineNo, "Units Sold");
		hasError = hasError || addErrorsIf(!isLong(unitsSold), errors, lineNo, "Units Sold", "Not a valid number : " + unitsSold);

		String unitPrice = csvRecord.get("Unit Price");
		hasError = hasError || addErrorsIfEmpty(unitPrice, errors, lineNo, "Unit Price");
		hasError = hasError || addErrorsIf(!isDouble(unitPrice), errors, lineNo, "Unit Price", "Not a valid number : " + unitPrice);

		String unitCost = csvRecord.get("Unit Cost");
		hasError = hasError || addErrorsIfEmpty(unitCost, errors, lineNo, "Unit Cost");
		hasError = hasError || addErrorsIf(!isDouble(unitCost), errors, lineNo, "Unit Cost", "Not a valid number : " + unitCost);

		String totalRevenue = csvRecord.get("Total Revenue");
		hasError = hasError || addErrorsIfEmpty(totalRevenue, errors, lineNo, "Total Revenue");
		hasError = hasError || addErrorsIf(!isDouble(totalRevenue), errors, lineNo, "Total Revenue", "Not a valid number : " + totalRevenue);

		String totalCost = csvRecord.get("Total Cost");
		hasError = hasError || addErrorsIfEmpty(totalCost, errors, lineNo, "Total Cost");
		hasError = hasError || addErrorsIf(!isDouble(totalCost), errors, lineNo, "Total Cost", "Not a valid number : " + totalCost);

		String totalProfit = csvRecord.get("Total Profit");
		hasError = hasError || addErrorsIfEmpty(totalProfit, errors, lineNo, "Total Profit");
		hasError = hasError || addErrorsIf(!isDouble(totalProfit), errors, lineNo, "Total Profit", "Not a valid number : " + totalProfit);

		if(!hasError) {
			try {
			order =  new Order()
					.setOrderId(Long.parseLong(orderId))
					.setOrderedBy(getNric())
					.setOrderDate(sdf.parse(orderDate))
					.setOrderPriority(orderPriority)
					.setCountry(getCountry(masterData, country))
					.setItemType(getItemType(masterData, itemType))
					.setRegion(getRegion(masterData, region))
					.setSalesChannel(getSalesChannel(masterData, salesChannel))
					.setShipDate(sdf.parse(shipDate))
					.setTotalCost(Double.parseDouble(totalCost))
					.setTotalProfit(Double.parseDouble(totalProfit))
					.setTotalRevenue(Double.parseDouble(totalRevenue))
					.setUnitCost(Double.parseDouble(unitCost))
					.setUnitPrice(Double.parseDouble(unitPrice))
					.setUnitsSold(Long.parseLong(unitsSold))
					.setNew(true);
			} catch (Exception e) {
				System.out.println("Error in line : " + lineNo + " : error : " + e.getMessage());
			}
		}
		return order;
	}

	private static SalesChannel getSalesChannel(CsvOrderProcesserVm masterData, String key) {
		if(masterData.salesChannelMap.containsKey(key))
			return masterData.salesChannelMap.get(key);
		SalesChannel x = new SalesChannel(key);
		masterData.salesChannelMap.put(key, x);
		return x;
	}

	private static Region getRegion(CsvOrderProcesserVm masterData, String key) {
		if(masterData.regionMap.containsKey(key))
			return masterData.regionMap.get(key);
		Region x = new Region(key);
		masterData.regionMap.put(key, x);
		return x;
	}

	private static ItemType getItemType(CsvOrderProcesserVm masterData, String key) {
		if(masterData.itemTypeMap.containsKey(key))
			return masterData.itemTypeMap.get(key);
		ItemType x = new ItemType(key);
		masterData.itemTypeMap.put(key, x);
		return x;
	}

	private static Country getCountry(CsvOrderProcesserVm masterData, String key) {
		if(masterData.countryMap.containsKey(key))
			return masterData.countryMap.get(key);
		Country x = new Country(key);
		masterData.countryMap.put(key, x);
		return x;
	}

	private static boolean isDouble(String val) {
		try {
			Double.parseDouble(val);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	private static boolean isLong(String val) {
		try {
			Long.parseLong(val);
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	public static void main(String[] args) {
		System.out.println(validDate("9/17/2010"));
	}

	private static boolean validDate(String val) {
		try {
			sdf.parse(val);
		}catch (Exception e) {
			return false;
		}
		return true;
	}

	private static String getNric() {
		String[] starting = {"S","T","F","G"};
		int index = getRandomNumberUsingNextInt(0,3);
		int val = getRandomNumberUsingNextInt(1111111,9999999);
		char c = (char)(new Random().nextInt(26) + 'a');
		String nric = starting[index] + val + c;
		System.out.println("nric : " +nric);
		
		return nric.toUpperCase();
	}
	
	public static int getRandomNumberUsingNextInt(int min, int max) {
	    Random random = new Random();
	    return random.nextInt(max - min) + min;
	}

	private static boolean addErrorsIfEmpty(String string, Map<String, Errors> errors, long lineNo, String fieldName) {
		String message = fieldName + " is mandatory";
		return addErrorsIf((string == null || string.trim().length() == 0), errors, lineNo, fieldName, message);

	}

	private static boolean addErrorsIf(boolean b, Map<String, Errors> errors, long lineNo, String fieldName, String message) {
		if(b) {
			addErrors(errors, lineNo, fieldName, message);
		}
		return b;
	}

	private static void addErrors(Map<String, Errors> errors, long lineNo, String fieldName, String message) {
		String key = ""+lineNo+"-"+fieldName;
		System.out.println(key + " : " + message);
		if(errors.containsKey(key)) {
			errors.get(key).addMessage(message);
		} else {
			errors.put(key, new Errors(lineNo, fieldName, message));
		}
	}
}
