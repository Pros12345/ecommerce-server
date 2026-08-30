
package eCommerse.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

	private static final Logger logger = LoggerFactory.getLogger(AdminUserServiceImpl.class);
	private static final String ADMIN_EMAIL_KEYWORD = "prosenjitchakrabortty";
	private final UserRepository userRepository;
	private final AddressRepository addressRepository;
	private final OrderRepository orderRepository;

	public AdminUserServiceImpl(UserRepository userRepository, AddressRepository addressRepository,
			OrderRepository orderRepository) {

		this.userRepository = userRepository;
		this.addressRepository = addressRepository;
		this.orderRepository = orderRepository;
	}

	@Override
	@Transactional(readOnly = true)
	public List<AdminUserResponse> getAllUsers(String loggedInEmail) {

		logger.info("AdminUserServiceImpl : getAllUsers :: Started");

		checkAdminEmail(loggedInEmail);
		List<User> users = userRepository.findAll();
		List<AdminUserResponse> response = new ArrayList<>();

		for (User user : users) {
			response.add(convertUserToResponse(user));
		}

		logger.info("AdminUserServiceImpl : getAllUsers :: Ended");

		return response;
	}

	@Override
	@Transactional
	public void deleteUser(Long userId, String loggedInEmail) {

		logger.info("AdminUserServiceImpl : deleteUser :: Started");

		checkAdminEmail(loggedInEmail);
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
		List<Order> orders = orderRepository.findOrdersByUser(user);

		for (Order order : orders) {
			orderRepository.deleteOrder(order);
		}
		List<Address> addresses = addressRepository.findByUser(user);

		if (!addresses.isEmpty()) {
			addressRepository.deleteAll(addresses);
			addressRepository.flush();
		}
		userRepository.delete(user);
		userRepository.flush();

		logger.info("AdminUserServiceImpl : deleteUser :: Ended");

	}

	private void checkAdminEmail(String email) {

		logger.info("AdminUserServiceImpl : checkAdminEmail :: Started");

		if (email == null || !email.toLowerCase().contains(ADMIN_EMAIL_KEYWORD)) {

			throw new AccessDeniedException("You are not authorized to access this resource");
		}

		logger.info("AdminUserServiceImpl : checkAdminEmail :: Ended");

	}

	private AdminUserResponse convertUserToResponse(User user) {

		logger.info("AdminUserServiceImpl : convertUserToResponse :: Started");

		AdminUserResponse response = new AdminUserResponse();
		response.setId(user.getId());
		response.setFirstName(user.getFirstName());
		response.setEmail(user.getEmail());
		response.setCountryCode(user.getCountryCode());
		response.setMobileNumber(user.getMobileNumber());
		List<AddressResponse> addresses = new ArrayList<>();
		List<Address> userAddresses = addressRepository.findByUser(user);
		for (Address address : userAddresses) {

			addresses.add(convertAddressToResponse(address));
		}
		response.setAddresses(addresses);
		List<OrderResponse> orders = new ArrayList<>();
		List<Order> userOrders = orderRepository.findOrdersByUser(user);

		for (Order order : userOrders) {
			orders.add(convertOrderToResponse(order));
		}
		response.setOrders(orders);

		logger.info("AdminUserServiceImpl : convertUserToResponse :: Ended");

		return response;
	}

	private AddressResponse convertAddressToResponse(Address address) {

		logger.info("AdminUserServiceImpl : convertAddressToResponse :: Started");

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

		logger.info("AdminUserServiceImpl : convertUserToResponse :: Ended");

		return response;
	}

	private OrderResponse convertOrderToResponse(Order order) {

		logger.info("AdminUserServiceImpl : convertOrderToResponse :: Started");

		OrderResponse response = new OrderResponse();
		response.setOrderId(order.getId());
		response.setTotalAmount(order.getTotalAmount());
		response.setPaymentMethod(order.getPaymentMethod());
		response.setOrderStatus(order.getOrderStatus());
		response.setOrderDate(order.getOrderDate());

		if (order.getAddress() != null) {
			response.setAddress(convertAddressToResponse(order.getAddress()));
		}

		List<OrderItemResponse> items = new ArrayList<>();

		if (order.getOrderItems() != null) {
			for (OrderItem orderItem : order.getOrderItems()) {
				OrderItemResponse itemResponse = convertOrderItemToResponse(orderItem);
				items.add(itemResponse);
			}
		}
		response.setItems(items);

		logger.info("AdminUserServiceImpl : convertOrderToResponse :: Ended");

		return response;
	}

	private OrderItemResponse convertOrderItemToResponse(OrderItem orderItem) {

		logger.info("AdminUserServiceImpl : convertOrderItemToResponse :: Started");

		OrderItemResponse response = new OrderItemResponse();
		Product product = orderItem.getProduct();
		if (product != null) {
			response.setProductId(product.getId());
			response.setProductName(product.getName());
			if (product.getImages() != null && !product.getImages().isEmpty()) {
				ProductImage primaryImage = product.getImages().stream().filter(ProductImage::isPrimaryImage)
						.findFirst().orElse(product.getImages().get(0));
				response.setImageId(primaryImage.getId());
			}
		}

		response.setQuantity(orderItem.getQuantity());
		response.setPrice(orderItem.getPrice());
		if (orderItem.getPrice() != null && orderItem.getQuantity() != null) {

			response.setTotal(orderItem.getPrice().multiply(java.math.BigDecimal.valueOf(orderItem.getQuantity())));
		}

		logger.info("AdminUserServiceImpl : convertOrderItemToResponse :: Ended");

		return response;
	}
}