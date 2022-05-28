package com.demo.ecommerce.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.demo.ecommerce.entity.Order;
import com.demo.ecommerce.vm.PaginatedOrder;

/**
 * @author User
 *
 */
@Repository
public class OrderDao {

	@Autowired
	private EntityManager em;

	public  List<Order> getpaginatedlist(PaginatedOrder paginatedOrder) {

		CriteriaBuilder cb = em.getCriteriaBuilder();

		CriteriaQuery<Order> cq = cb.createQuery(Order.class);
		Root<Order> root = cq.from(Order.class);
			cq.orderBy(cb.asc(root.get("id")));

		TypedQuery<Order> query = em.createQuery(cq);
		List<Order> orders = query.setFirstResult(paginatedOrder.getPageIndex() )
				.setMaxResults(paginatedOrder.getPageSize())
				.getResultList();
		
		return orders;

	}

}
