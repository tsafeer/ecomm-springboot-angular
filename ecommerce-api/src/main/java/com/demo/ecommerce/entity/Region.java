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
@Table(name = "region")
public class Region {
	
	public Region() {}
	
	public Region(String regionName) {
		this.regionName = regionName;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="region_id")
	private Long regionId;
	
	@Column(name="region_name")
	private String regionName;
}
