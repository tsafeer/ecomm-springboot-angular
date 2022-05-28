/**
 * 
 */
package com.demo.ecommerce.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.demo.ecommerce.entity.Order;

/**
 * @author User
 *
 */
@Repository
public interface OrderRepository extends PagingAndSortingRepository<Order, Long> {

}
