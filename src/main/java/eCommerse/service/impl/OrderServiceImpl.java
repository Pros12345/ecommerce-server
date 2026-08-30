package eCommerse.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import eCommerse.entity.ProductImage;
import eCommerse.entity.User;
import eCommerse.repository.AddressRepository;
import eCommerse.repository.OrderRepository;
import eCommerse.repository.ProductsRepository;
import eCommerse.repository.UserRepository;
import eCommerse.service.OrderService;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

	private static final Logger logger = LoggerFactory.getLogger(AdminUserServiceImpl.class);
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

	@Override
	public OrderResponse placeOrder(OrderRequest request) {

		logger.info("OrderServiceImpl : placeOrder :: Started");

		User user = getLoggedInUser();
		if (!"COD".equalsIgnoreCase(request.getPaymentMethod())) {
			throw new RuntimeException("Only Cash on Delivery is allowed");
		}

		Address address = addressRepository.findByIdAndUser(request.getAddressId(), user)
				.orElseThrow(() -> new RuntimeException("Address not found"));

		Order order = new Order();
		order.setUser(user);
		order.setAddress(address);
		order.setPaymentMethod("COD");
		order.setOrderStatus("PLACED");
		order.setOrderDate(LocalDateTime.now());
		BigDecimal totalAmount = BigDecimal.ZERO;
		List<OrderItem> orderItems = new ArrayList<>();

		for (OrderItemRequest itemRequest : request.getItems()) {
			if (itemRequest.getQuantity() <= 0) {
				throw new RuntimeException("Invalid quantity");
			}
			Product product = getProduct(itemRequest.getProductId());
			int updatedRows = productsRepository.reduceStock(product.getId(), itemRequest.getQuantity());
			if (updatedRows == 0) {
				throw new RuntimeException("Insufficient stock for product: " + product.getName());
			}

			OrderItem orderItem = new OrderItem();
			orderItem.setOrder(order);
			orderItem.setProduct(product);
			orderItem.setQuantity(itemRequest.getQuantity());
			orderItem.setPrice(BigDecimal.valueOf(product.getPrice()));
			orderItems.add(orderItem);
			BigDecimal itemTotal = BigDecimal.valueOf(product.getPrice())
					.multiply(BigDecimal.valueOf(itemRequest.getQuantity()));
			totalAmount = totalAmount.add(itemTotal);
		}

		order.setOrderItems(orderItems);
		order.setTotalAmount(totalAmount);
		Order savedOrder = orderRepository.saveOrder(order);

		logger.info("OrderServiceImpl : placeOrder :: Ended");

		return convertToResponse(savedOrder, address);
	}

	private User getLoggedInUser() {

		logger.info("OrderServiceImpl : getLoggedInUser :: Started");

		String email = SecurityContextHolder.getContext().getAuthentication().getName();

		logger.info("OrderServiceImpl : getLoggedInUser :: Ended");

		return userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
	}

	private Product getProduct(Long productId) {

		logger.info("OrderServiceImpl : getProduct :: Started");

		Product product = productsRepository.findById(productId);

		if (product == null) {
			throw new RuntimeException("Product not found with id: " + productId);
		}

		logger.info("OrderServiceImpl : getProduct :: Ended");

		return product;
	}

	private OrderResponse convertToResponse(Order order, Address address) {

		logger.info("OrderServiceImpl : convertToResponse :: Started");

		OrderResponse response = new OrderResponse();
		response.setOrderId(order.getId());
		response.setTotalAmount(order.getTotalAmount());
		response.setPaymentMethod(order.getPaymentMethod());
		response.setOrderStatus(order.getOrderStatus());
		response.setOrderDate(order.getOrderDate());

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

		List<OrderItemResponse> items = new ArrayList<>();
		for (OrderItem orderItem : order.getOrderItems()) {
			OrderItemResponse itemResponse = new OrderItemResponse();
			Product product = orderItem.getProduct();

			itemResponse.setProductId(product.getId());
			itemResponse.setProductName(product.getName());
			itemResponse.setQuantity(orderItem.getQuantity());
			itemResponse.setPrice(orderItem.getPrice());
			BigDecimal itemTotal = orderItem.getPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity()));
			itemResponse.setTotal(itemTotal);

			if (product.getImages() != null && !product.getImages().isEmpty()) {
				ProductImage primaryImage = product.getImages().stream().filter(ProductImage::isPrimaryImage)
						.findFirst().orElse(product.getImages().get(0));
				itemResponse.setImageId(primaryImage.getId());
			}
			items.add(itemResponse);
		}
		response.setItems(items);

		logger.info("OrderServiceImpl : convertToResponse :: Ended");

		return response;
	}

	@Override
	public List<OrderResponse> getMyOrders() {

		logger.info("OrderServiceImpl : getMyOrders :: Started");

		User user = getLoggedInUser();
		List<Order> orders = orderRepository.findOrdersByUser(user);
		List<OrderResponse> responses = new ArrayList<>();
		for (Order order : orders) {
			responses.add(convertToResponse(order, order.getAddress()));
		}

		logger.info("OrderServiceImpl : getMyOrders :: Ended");

		return responses;
	}

	@Override
	public OrderResponse cancelOrder(Long orderId) {

		logger.info("OrderServiceImpl : cancelOrder :: Started");

		User user = getLoggedInUser();
		Order order = orderRepository.findOrderByIdAndUser(orderId, user);
		if (order == null) {
			throw new RuntimeException("Order not found");
		}

		if ("CANCELLED".equalsIgnoreCase(order.getOrderStatus())) {
			throw new RuntimeException("Order is already cancelled");
		}

		for (OrderItem orderItem : order.getOrderItems()) {
			Product product = orderItem.getProduct();

			if (product == null) {
				throw new RuntimeException("Product not found for order item");
			}

			int quantity = orderItem.getQuantity();

			if (quantity <= 0) {
				throw new RuntimeException("Invalid order item quantity");
			}

			int updatedRows = productsRepository.increaseStock(product.getId(), quantity);

			if (updatedRows == 0) {
				throw new RuntimeException("Unable to restore stock for product: " + product.getName());
			}
		}

		order.setOrderStatus("CANCELLED");

		logger.info("OrderServiceImpl : cancelOrder :: Ended");

		return convertToResponse(order, order.getAddress());
	}

	@Override
	public void deleteCancelledOrder(Long orderId) {

		logger.info("OrderServiceImpl : deleteCancelledOrder :: Started");

		User user = getLoggedInUser();
		Order order = orderRepository.findOrderByIdAndUser(orderId, user);

		if (order == null) {
			throw new RuntimeException("Order not found");
		}

		if (!"CANCELLED".equalsIgnoreCase(order.getOrderStatus())) {
			throw new RuntimeException("Only cancelled orders can be deleted");
		}

		orderRepository.deleteOrder(order);

		logger.info("OrderServiceImpl : deleteCancelledOrder :: Ended");

	}
}