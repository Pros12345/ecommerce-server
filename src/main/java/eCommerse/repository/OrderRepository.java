package eCommerse.repository;

import java.util.List;

import eCommerse.entity.Order;
import eCommerse.entity.User;

public interface OrderRepository {

	Order saveOrder(Order order);

	List<Order> findOrdersByUser(User user);

	Order findOrderByIdAndUser(Long orderId, User user);

	void deleteOrder(Order order);
}