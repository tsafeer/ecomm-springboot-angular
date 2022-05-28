/**
 * 
 */
package com.demo.ecommerce.entity;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.data.domain.Persistable;

import com.demo.ecommerce.vm.OrderVm;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author User
 *
 */
@Accessors(chain = true)
@Data
@Entity
@Table(name = "orders")
public class Order implements Persistable<Long>{
	
	@Transient
	private boolean isNew = false;
	
	//Order_ID number(13,0),
	@Id
	@Column(name="Order_ID")
	private long orderId;

	//ordered_by varchar2(9),
	@Column(name="ordered_by")
	private String orderedBy;

	//Order_Date date,
	@Column(name="Order_Date")
	private Date orderDate;
	
	//Order_Priority varchar2(1),
	@Column(name="Order_Priority")
	private String orderPriority;
	
	//Region_id number(3),
	@OneToOne(cascade = {CascadeType.MERGE})
	@JoinColumn(name ="Region_id")
	private Region region ;
	
	//Country_id number(3),
	@OneToOne(cascade = {CascadeType.MERGE})
	@JoinColumn(name="Country_id")
	private Country country;
	
	//Item_Type_id number(3),
	@OneToOne(cascade = {CascadeType.MERGE})
	@JoinColumn(name="Item_Type_id")
	private ItemType itemType;
	
	//Sales_Channel_id number(3),
	@OneToOne(cascade = {CascadeType.MERGE})
	@JoinColumn(name="Sales_Channel_id")
	private SalesChannel salesChannel;
	
	//Ship_Date date,
	@Column(name="Ship_Date")
	private Date shipDate;
	
	//Units_Sold number(10,0),
	@Column(name="Units_Sold")
	private long unitsSold;
	
	//Unit_Price number(13,2),
	@Column(name="Unit_Price")
	private double unitPrice;
	
	//Unit_Cost number(13,2),
	@Column(name="Unit_Cost")
	private double unitCost;
	
	//Total_Revenue number(13,2),
	@Column(name="Total_Revenue")
	private double totalRevenue;
	
	//Total_Cost number(13,2),
	@Column(name="Total_Cost")
	private double totalCost;
	
	//Total_Profit number(13,2)
	@Column(name="Total_Profit")
	private double totalProfit;
	
	public OrderVm getOrderVm() {
		return new OrderVm()
				.setOrderID(orderId)
				.setOrderedBy(orderedBy)
				.setOrderDate(orderDate)
				.setOrderPriority(orderPriority)
				.setCountry(country != null ? country.getCountryName() : "")
				.setItemType(itemType != null ? itemType.getItemTypeName() : "")
				.setRegion(region != null ? region.getRegionName() : "")
				.setSalesChannel(salesChannel != null ? salesChannel.getSalesChannelName() : "")
				.setShipDate(shipDate)
				.setTotalCost(totalCost)
				.setTotalProfit(totalProfit)
				.setTotalRevenue(totalRevenue)
				.setUnitCost(unitCost)
				.setUnitPrice(unitPrice)
				.setUnitsSold(unitsSold);

	}

	@Override
	public Long getId() {
		return orderId;
	}

	@Override
	public boolean isNew() {
		return isNew;
	}
}
