package eCommerse.repository.impl;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import eCommerse.entity.Order;
import eCommerse.repository.OrderRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Repository
@Transactional
public class OrderRepositoryImpl implements OrderRepository {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Order saveOrder(Order order) {

		entityManager.persist(order);

		entityManager.flush();

		return order;
	}
}