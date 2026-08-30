package eCommerse.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import eCommerse.entity.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

}