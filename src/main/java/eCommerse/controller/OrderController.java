package eCommerse.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import eCommerse.dto.OrderRequest;
import eCommerse.dto.OrderResponse;
import eCommerse.service.OrderService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

	private final OrderService orderService;

	public OrderController(OrderService orderService) {

		this.orderService = orderService;
	}

	// ==========================================
	// PLACE ORDER
	// ==========================================

	@PostMapping
	public ResponseEntity<OrderResponse> placeOrder(@Valid @RequestBody OrderRequest request) {

		OrderResponse response = orderService.placeOrder(request);

		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
}