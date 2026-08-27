package eCommerse.repository.impl;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import eCommerse.entity.Order;
import eCommerse.entity.User;
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

	@Override
	public List<Order> findOrdersByUser(User user) {

		String jpql = """
				SELECT DISTINCT o
				FROM Order o
				LEFT JOIN FETCH o.orderItems oi
				LEFT JOIN FETCH oi.product
				LEFT JOIN FETCH o.address
				WHERE o.user = :user
				ORDER BY o.orderDate DESC
				""";

		return entityManager.createQuery(jpql, Order.class).setParameter("user", user).getResultList();
	}

	@Override
	public Order findOrderByIdAndUser(Long orderId, User user) {

		String jpql = """
				SELECT DISTINCT o
				FROM Order o
				LEFT JOIN FETCH o.orderItems oi
				LEFT JOIN FETCH oi.product
				LEFT JOIN FETCH o.address
				WHERE o.id = :orderId
				AND o.user = :user
				""";

		List<Order> orders = entityManager.createQuery(jpql, Order.class).setParameter("orderId", orderId)
				.setParameter("user", user).getResultList();

		return orders.isEmpty() ? null : orders.get(0);
	}

	@Override
	public void deleteOrder(Order order) {

		entityManager.remove(order);

		entityManager.flush();
	}
}