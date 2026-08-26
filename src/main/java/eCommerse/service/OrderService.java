package eCommerse.service;

import eCommerse.dto.OrderRequest;
import eCommerse.dto.OrderResponse;

public interface OrderService {

	OrderResponse placeOrder(OrderRequest request);

}