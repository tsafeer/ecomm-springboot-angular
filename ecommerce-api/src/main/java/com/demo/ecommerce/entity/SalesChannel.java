/**
 * 
 */
package com.demo.ecommerce.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * @author User
 *
 */
@Data
@Entity
@Table(name = "Sales_Channel")
public class SalesChannel {

	public SalesChannel() {}
	
	public SalesChannel(String salesChannelName) {
		this.salesChannelName = salesChannelName;
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="Sales_Channel_id")
	private Long salesChannelId;
	
	@Column(name="Sales_Channel_name")
	private String salesChannelName;
}
