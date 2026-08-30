package eCommerse.service;

import java.util.List;

import eCommerse.dto.OrderRequest;
import eCommerse.dto.OrderResponse;

public interface OrderService {

	OrderResponse placeOrder(OrderRequest request);

	List<OrderResponse> getMyOrders();

	OrderResponse cancelOrder(Long orderId);

	void deleteCancelledOrder(Long orderId);

}