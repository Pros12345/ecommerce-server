package eCommerse.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import eCommerse.dto.AddressResponse;
import eCommerse.dto.OrderItemRequest;
import eCommerse.dto.OrderItemResponse;
import eCommerse.dto.OrderRequest;
import eCommerse.dto.OrderResponse;
import eCommerse.entity.Address;
import eCommerse.entity.Order;
import eCommerse.entity.OrderItem;
import eCommerse.entity.Product;
import eCommerse.entity.User;
import eCommerse.repository.AddressRepository;
import eCommerse.repository.OrderRepository;
import eCommerse.repository.ProductsRepository;
import eCommerse.repository.UserRepository;
import eCommerse.service.OrderService;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

	private final OrderRepository orderRepository;

	private final ProductsRepository productsRepository;

	private final AddressRepository addressRepository;

	private final UserRepository userRepository;

	public OrderServiceImpl(OrderRepository orderRepository, ProductsRepository productsRepository,
			AddressRepository addressRepository, UserRepository userRepository) {

		this.orderRepository = orderRepository;

		this.productsRepository = productsRepository;

		this.addressRepository = addressRepository;

		this.userRepository = userRepository;
	}

	// ==========================================
	// PLACE ORDER
	// ==========================================

	@Override
	public OrderResponse placeOrder(OrderRequest request) {

		// --------------------------------------
		// GET LOGGED IN USER
		// --------------------------------------

		User user = getLoggedInUser();

		// --------------------------------------
		// ONLY COD ALLOWED
		// --------------------------------------

		if (!"COD".equalsIgnoreCase(request.getPaymentMethod())) {

			throw new RuntimeException("Only Cash on Delivery is allowed");
		}

		// --------------------------------------
		// GET USER ADDRESS
		// --------------------------------------

		Address address = addressRepository.findByIdAndUser(request.getAddressId(), user)
				.orElseThrow(() -> new RuntimeException("Address not found"));

		// --------------------------------------
		// CREATE ORDER
		// --------------------------------------

		Order order = new Order();

		order.setUser(user);

		order.setAddress(address);

		order.setPaymentMethod("COD");

		order.setOrderStatus("PLACED");

		order.setOrderDate(LocalDateTime.now());

		BigDecimal totalAmount = BigDecimal.ZERO;

		List<OrderItem> orderItems = new ArrayList<>();

		// --------------------------------------
		// PROCESS CART ITEMS
		// --------------------------------------

		for (OrderItemRequest itemRequest : request.getItems()) {

			if (itemRequest.getQuantity() <= 0) {

				throw new RuntimeException("Invalid quantity");
			}

			Product product = getProduct(itemRequest.getProductId());

			// ----------------------------------
			// REDUCE STOCK
			// ----------------------------------

			int updatedRows = productsRepository.reduceStock(product.getId(), itemRequest.getQuantity());

			if (updatedRows == 0) {

				throw new RuntimeException("Insufficient stock for product: " + product.getName());
			}

			// ----------------------------------
			// CREATE ORDER ITEM
			// ----------------------------------

			OrderItem orderItem = new OrderItem();

			orderItem.setOrder(order);

			orderItem.setProduct(product);

			orderItem.setQuantity(itemRequest.getQuantity());

			orderItem.setPrice(BigDecimal.valueOf(product.getPrice()));

			orderItems.add(orderItem);

			// ----------------------------------
			// CALCULATE TOTAL
			// ----------------------------------

			BigDecimal itemTotal = BigDecimal.valueOf(product.getPrice())
					.multiply(BigDecimal.valueOf(itemRequest.getQuantity()));

			totalAmount = totalAmount.add(itemTotal);
		}

		// --------------------------------------
		// SET ORDER ITEMS
		// --------------------------------------

		order.setOrderItems(orderItems);

		order.setTotalAmount(totalAmount);

		// --------------------------------------
		// SAVE ORDER
		// --------------------------------------

		Order savedOrder = orderRepository.saveOrder(order);

		// --------------------------------------
		// RESPONSE
		// --------------------------------------

		return convertToResponse(savedOrder, address);
	}

	// ==========================================
	// GET USER
	// ==========================================

	private User getLoggedInUser() {

		String email = SecurityContextHolder.getContext().getAuthentication().getName();

		return userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
	}

	// ==========================================
	// GET PRODUCT
	// ==========================================

	private Product getProduct(Long productId) {

		Product product = productsRepository.findById(productId);

		if (product == null) {

			throw new RuntimeException("Product not found with id: " + productId);
		}

		return product;
	}

	// ==========================================
	// CONVERT RESPONSE
	// ==========================================

	private OrderResponse convertToResponse(Order order, Address address) {

		OrderResponse response = new OrderResponse();

		response.setOrderId(order.getId());

		response.setTotalAmount(order.getTotalAmount());

		response.setPaymentMethod(order.getPaymentMethod());

		response.setOrderStatus(order.getOrderStatus());

		response.setOrderDate(order.getOrderDate());

		// --------------------------------------
		// ADDRESS RESPONSE
		// --------------------------------------

		AddressResponse addressResponse = new AddressResponse();

		addressResponse.setId(address.getId());

		addressResponse.setFullName(address.getFullName());

		addressResponse.setMobileNumber(address.getMobileNumber());

		addressResponse.setAddressLine1(address.getAddressLine1());

		addressResponse.setAddressLine2(address.getAddressLine2());

		addressResponse.setCity(address.getCity());

		addressResponse.setState(address.getState());

		addressResponse.setPincode(address.getPincode());

		addressResponse.setLandmark(address.getLandmark());

		addressResponse.setAddressType(address.getAddressType());

		response.setAddress(addressResponse);

		// --------------------------------------
		// ORDER ITEMS RESPONSE
		// --------------------------------------

		List<OrderItemResponse> items = new ArrayList<>();

		for (OrderItem orderItem : order.getOrderItems()) {

			OrderItemResponse itemResponse = new OrderItemResponse();

			Product product = orderItem.getProduct();

			// ==========================================
			// PRODUCT DETAILS
			// ==========================================

			itemResponse.setProductId(product.getId());

			itemResponse.setProductName(product.getName());

			itemResponse.setQuantity(orderItem.getQuantity());

			itemResponse.setPrice(orderItem.getPrice());

			// ==========================================
			// ITEM TOTAL
			// ==========================================

			BigDecimal itemTotal = orderItem.getPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity()));

			itemResponse.setTotal(itemTotal);

			// ==========================================
			// FIRST PRODUCT IMAGE
			// ==========================================

			if (product.getImages() != null && !product.getImages().isEmpty()) {

				itemResponse.setImageId(product.getImages().get(0).getId());
			}

			items.add(itemResponse);
		}

		response.setItems(items);

		return response;
	}

	@Override
	public List<OrderResponse> getMyOrders() {

		User user = getLoggedInUser();

		List<Order> orders = orderRepository.findOrdersByUser(user);

		List<OrderResponse> responses = new ArrayList<>();

		for (Order order : orders) {

			responses.add(convertToResponse(order, order.getAddress()));
		}

		return responses;
	}

	@Override
	public OrderResponse cancelOrder(Long orderId) {

		User user = getLoggedInUser();

		Order order = orderRepository.findOrderByIdAndUser(orderId, user);

		if (order == null) {

			throw new RuntimeException("Order not found");
		}

		if ("CANCELLED".equalsIgnoreCase(order.getOrderStatus())) {

			throw new RuntimeException("Order is already cancelled");
		}

		order.setOrderStatus("CANCELLED");

		return convertToResponse(order, order.getAddress());
	}

	@Override
	public void deleteCancelledOrder(Long orderId) {

		User user = getLoggedInUser();

		Order order = orderRepository.findOrderByIdAndUser(orderId, user);

		if (order == null) {

			throw new RuntimeException("Order not found");
		}

		if (!"CANCELLED".equalsIgnoreCase(order.getOrderStatus())) {

			throw new RuntimeException("Only cancelled orders can be deleted");
		}

		orderRepository.deleteOrder(order);
	}
}