package eCommerse.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

	@GetMapping("/my-orders")
	public ResponseEntity<List<OrderResponse>> getMyOrders() {

		List<OrderResponse> orders = orderService.getMyOrders();

		return ResponseEntity.ok(orders);
	}

	@PutMapping("/{orderId}/cancel")
	public ResponseEntity<OrderResponse> cancelOrder(@PathVariable Long orderId) {

		OrderResponse response = orderService.cancelOrder(orderId);

		return ResponseEntity.ok(response);
	}

	@DeleteMapping("/{orderId}")
	public ResponseEntity<Void> deleteOrder(@PathVariable Long orderId) {

		orderService.deleteCancelledOrder(orderId);

		return ResponseEntity.noContent().build();
	}
}