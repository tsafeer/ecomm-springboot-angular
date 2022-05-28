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
@Table(name = "Item_Type")
public class ItemType {

	public ItemType() {}
	
	public ItemType(String itemTypeName) {
		this.itemTypeName = itemTypeName;
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="Item_Type_id")
	private Long itemTypeId;
	
	@Column(name="Item_Type_name")
	private String itemTypeName;
}
