/**
 * 
 */
package com.demo.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.demo.ecommerce.entity.Country;

/**
 * @author User
 *
 */
public interface CountryRepository extends JpaRepository<Country, Long> {

}
