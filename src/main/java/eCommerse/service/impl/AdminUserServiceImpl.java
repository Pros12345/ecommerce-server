
package eCommerse.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import eCommerse.dto.AddressResponse;
import eCommerse.dto.AdminUserResponse;
import eCommerse.dto.OrderItemResponse;
import eCommerse.dto.OrderResponse;
import eCommerse.entity.Address;
import eCommerse.entity.Order;
import eCommerse.entity.OrderItem;
import eCommerse.entity.Product;
import eCommerse.entity.ProductImage;
import eCommerse.entity.User;
import eCommerse.repository.AddressRepository;
import eCommerse.repository.OrderRepository;
import eCommerse.repository.UserRepository;
import eCommerse.service.AdminUserService;

@Service
@Transactional
public class AdminUserServiceImpl implements AdminUserService {

	// =====================================================
	// ADMIN EMAIL KEYWORD
	// =====================================================

	private static final String ADMIN_EMAIL_KEYWORD = "prosenjitchakrabortty";

	// =====================================================
	// REPOSITORIES
	// =====================================================

	private final UserRepository userRepository;

	private final AddressRepository addressRepository;

	private final OrderRepository orderRepository;

	// =====================================================
	// CONSTRUCTOR
	// =====================================================

	public AdminUserServiceImpl(UserRepository userRepository, AddressRepository addressRepository,
			OrderRepository orderRepository) {

		this.userRepository = userRepository;
		this.addressRepository = addressRepository;
		this.orderRepository = orderRepository;
	}

	// =====================================================
	// GET ALL USERS
	// =====================================================

	@Override
	@Transactional(readOnly = true)
	public List<AdminUserResponse> getAllUsers(String loggedInEmail) {

		// -------------------------------------------------
		// CHECK ADMIN EMAIL
		// -------------------------------------------------

		checkAdminEmail(loggedInEmail);

		// -------------------------------------------------
		// GET ALL USERS
		// -------------------------------------------------

		List<User> users = userRepository.findAll();

		List<AdminUserResponse> response = new ArrayList<>();

		for (User user : users) {

			response.add(convertUserToResponse(user));
		}

		return response;
	}

	// =====================================================
	// DELETE USER
	// =====================================================

	@Override
	@Transactional
	public void deleteUser(Long userId, String loggedInEmail) {

		checkAdminEmail(loggedInEmail);

		User user = userRepository.findById(userId)
				.orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

		// 1. Delete user's orders
		List<Order> orders = orderRepository.findOrdersByUser(user);

		for (Order order : orders) {
			orderRepository.deleteOrder(order);
		}

		// 2. Delete user's addresses
		List<Address> addresses = addressRepository.findByUser(user);

		if (!addresses.isEmpty()) {
			addressRepository.deleteAll(addresses);
			addressRepository.flush();
		}

		// 3. Delete user
		userRepository.delete(user);
		userRepository.flush();
	}

	// =====================================================
	// ADMIN EMAIL VALIDATION
	// =====================================================

	private void checkAdminEmail(String email) {

		if (email == null || !email.toLowerCase().contains(ADMIN_EMAIL_KEYWORD)) {

			throw new AccessDeniedException("You are not authorized to access this resource");
		}
	}

	// =====================================================
	// CONVERT USER
	// =====================================================

	private AdminUserResponse convertUserToResponse(User user) {

		AdminUserResponse response = new AdminUserResponse();

		// -------------------------------------------------
		// USER DETAILS
		// -------------------------------------------------

		response.setId(user.getId());

		response.setFirstName(user.getFirstName());

		response.setEmail(user.getEmail());

		response.setCountryCode(user.getCountryCode());

		response.setMobileNumber(user.getMobileNumber());

		// -------------------------------------------------
		// ADDRESSES
		// -------------------------------------------------

		List<AddressResponse> addresses = new ArrayList<>();

		List<Address> userAddresses = addressRepository.findByUser(user);

		for (Address address : userAddresses) {

			addresses.add(convertAddressToResponse(address));
		}

		response.setAddresses(addresses);

		// -------------------------------------------------
		// ORDERS
		// -------------------------------------------------

		List<OrderResponse> orders = new ArrayList<>();

		List<Order> userOrders = orderRepository.findOrdersByUser(user);

		for (Order order : userOrders) {

			orders.add(convertOrderToResponse(order));
		}

		response.setOrders(orders);

		return response;
	}

	// =====================================================
	// CONVERT ADDRESS
	// =====================================================

	private AddressResponse convertAddressToResponse(Address address) {

		AddressResponse response = new AddressResponse();

		response.setId(address.getId());

		response.setFullName(address.getFullName());

		response.setMobileNumber(address.getMobileNumber());

		response.setAddressLine1(address.getAddressLine1());

		response.setAddressLine2(address.getAddressLine2());

		response.setCity(address.getCity());

		response.setState(address.getState());

		response.setPincode(address.getPincode());

		response.setLandmark(address.getLandmark());

		response.setAddressType(address.getAddressType());

		return response;
	}

	// =====================================================
	// CONVERT ORDER
	// =====================================================

	private OrderResponse convertOrderToResponse(Order order) {

		OrderResponse response = new OrderResponse();

		// -------------------------------------------------
		// ORDER DETAILS
		// -------------------------------------------------

		response.setOrderId(order.getId());

		response.setTotalAmount(order.getTotalAmount());

		response.setPaymentMethod(order.getPaymentMethod());

		response.setOrderStatus(order.getOrderStatus());

		response.setOrderDate(order.getOrderDate());

		// -------------------------------------------------
		// ORDER ADDRESS
		// -------------------------------------------------

		if (order.getAddress() != null) {

			response.setAddress(convertAddressToResponse(order.getAddress()));
		}

		// -------------------------------------------------
		// ORDER ITEMS
		// -------------------------------------------------

		List<OrderItemResponse> items = new ArrayList<>();

		if (order.getOrderItems() != null) {

			for (OrderItem orderItem : order.getOrderItems()) {

				OrderItemResponse itemResponse = convertOrderItemToResponse(orderItem);

				items.add(itemResponse);
			}
		}

		response.setItems(items);

		return response;
	}

	// =====================================================
	// CONVERT ORDER ITEM
	// =====================================================

	private OrderItemResponse convertOrderItemToResponse(OrderItem orderItem) {

		OrderItemResponse response = new OrderItemResponse();

		// -------------------------------------------------
		// PRODUCT
		// -------------------------------------------------

		Product product = orderItem.getProduct();

		if (product != null) {

			response.setProductId(product.getId());

			response.setProductName(product.getName());

			// -------------------------------------------------
			// PRODUCT IMAGE
			// -------------------------------------------------

			if (product.getImages() != null && !product.getImages().isEmpty()) {

				ProductImage primaryImage = product.getImages().stream().filter(ProductImage::isPrimaryImage)
						.findFirst().orElse(product.getImages().get(0));

				response.setImageId(primaryImage.getId());
			}
		}

		// -------------------------------------------------
		// QUANTITY
		// -------------------------------------------------

		response.setQuantity(orderItem.getQuantity());

		// -------------------------------------------------
		// PRICE
		// -------------------------------------------------

		response.setPrice(orderItem.getPrice());

		// -------------------------------------------------
		// TOTAL
		// -------------------------------------------------

		if (orderItem.getPrice() != null && orderItem.getQuantity() != null) {

			response.setTotal(orderItem.getPrice().multiply(java.math.BigDecimal.valueOf(orderItem.getQuantity())));
		}

		return response;
	}
}