/**
 * 
 */
package com.demo.ecommerce.vm;

import java.util.Date;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author User
 *
 */
@Accessors(chain = true)
@Data
public class OrderVm {

	private long orderID;
	private String orderedBy;
	private Date orderDate;
	private String orderPriority;	
	private String region;
	private String country;
	private String itemType;
	private String salesChannel;
	private Date shipDate;
	private long unitsSold;
	private double unitPrice;
	private double unitCost;
	private double totalRevenue;
	private double totalCost;
	private double totalProfit;
}
