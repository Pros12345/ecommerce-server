package eCommerse.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

	private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

	private final OrderService orderService;

	public OrderController(OrderService orderService) {

		this.orderService = orderService;
	}

	@PostMapping
	public ResponseEntity<OrderResponse> placeOrder(@Valid @RequestBody OrderRequest request) {

		logger.info("OrderController : placeOrder :: Started");

		OrderResponse response = orderService.placeOrder(request);

		logger.info("OrderController : placeOrder :: Ended");

		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@GetMapping("/my-orders")
	public ResponseEntity<List<OrderResponse>> getMyOrders() {

		logger.info("OrderController : getMyOrders :: Started");

		List<OrderResponse> orders = orderService.getMyOrders();

		logger.info("OrderController : getMyOrders :: Ended");

		return ResponseEntity.ok(orders);
	}

	@PutMapping("/{orderId}/cancel")
	public ResponseEntity<OrderResponse> cancelOrder(@PathVariable Long orderId) {

		logger.info("OrderController : cancelOrder :: Started");

		OrderResponse response = orderService.cancelOrder(orderId);

		logger.info("OrderController : cancelOrder :: Ended");

		return ResponseEntity.ok(response);
	}

	@DeleteMapping("/{orderId}")
	public ResponseEntity<Void> deleteOrder(@PathVariable Long orderId) {

		logger.info("OrderController : deleteOrder :: Started");

		orderService.deleteCancelledOrder(orderId);

		logger.info("OrderController : deleteOrder :: Ended");

		return ResponseEntity.noContent().build();
	}
}