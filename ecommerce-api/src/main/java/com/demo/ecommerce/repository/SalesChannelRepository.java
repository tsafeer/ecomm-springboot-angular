/**
 * 
 */
package com.demo.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.demo.ecommerce.entity.SalesChannel;

/**
 * @author User
 *
 */
public interface SalesChannelRepository extends JpaRepository<SalesChannel, Long> {

}
