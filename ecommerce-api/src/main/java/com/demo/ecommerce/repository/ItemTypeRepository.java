/**
 * 
 */
package com.demo.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.demo.ecommerce.entity.ItemType;

/**
 * @author User
 *
 */
public interface ItemTypeRepository extends JpaRepository<ItemType, Long> {

}
