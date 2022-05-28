/**
 * 
 */
package com.demo.ecommerce.service;

import java.util.Map;

import com.demo.ecommerce.entity.Country;
import com.demo.ecommerce.entity.ItemType;
import com.demo.ecommerce.entity.Region;
import com.demo.ecommerce.entity.SalesChannel;
import com.demo.ecommerce.vm.Errors;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author User
 *
 */
@Data
@Accessors(chain = true)
public class CsvOrderProcesserVm {
	
	protected Map<String, Errors> errors;
	protected Map<String,Country> countryMap;
	protected Map<String,Region> regionMap;
	protected Map<String,ItemType> itemTypeMap;
	protected Map<String,SalesChannel> salesChannelMap;

}
