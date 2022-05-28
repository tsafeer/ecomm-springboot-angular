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
@Table(name = "country")
public class Country {
	
	public Country() {}
	
	public Country(String countryName) {
		this.countryName = countryName;
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="country_id")
	private Long countryId;
	
	@Column(name="country_name")
	private String countryName;
	
}
