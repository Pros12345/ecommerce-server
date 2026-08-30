package eCommerse.repository.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

	private static final Logger logger = LoggerFactory.getLogger(OrderRepositoryImpl.class);

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Order saveOrder(Order order) {

		logger.info("OrderRepositoryImpl : saveOrder :: Started");

		entityManager.persist(order);
		entityManager.flush();

		logger.info("OrderRepositoryImpl : saveOrder :: Ended");

		return order;
	}

	@Override
	public List<Order> findOrdersByUser(User user) {

		logger.info("OrderRepositoryImpl : findOrdersByUser :: Started");

		String jpql = """
				SELECT DISTINCT o
				FROM Order o
				LEFT JOIN FETCH o.orderItems oi
				LEFT JOIN FETCH oi.product
				LEFT JOIN FETCH o.address
				WHERE o.user = :user
				ORDER BY o.orderDate DESC
				""";

		logger.info("OrderRepositoryImpl : findOrdersByUser :: Ended");

		return entityManager.createQuery(jpql, Order.class).setParameter("user", user).getResultList();
	}

	@Override
	public Order findOrderByIdAndUser(Long orderId, User user) {

		logger.info("OrderRepositoryImpl : findOrderByIdAndUser :: Started");

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

		logger.info("OrderRepositoryImpl : findOrderByIdAndUser :: Ended");

		return orders.isEmpty() ? null : orders.get(0);
	}

	@Override
	public void deleteOrder(Order order) {

		logger.info("OrderRepositoryImpl : deleteOrder :: Started");

		entityManager.remove(order);
		entityManager.flush();

		logger.info("OrderRepositoryImpl : deleteOrder :: Ended");
	}
}