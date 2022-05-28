/**
 * 
 */
package com.demo.ecommerce.vm;

import java.util.List;

import lombok.Data;

/**
 * @author User
 *
 */
@Data
public class PaginatedOrder {
	
	private long length;
	private int pageIndex;
	private int pageSize;
	private int previousPageIndex;
	private List<OrderVm> orders;
}
