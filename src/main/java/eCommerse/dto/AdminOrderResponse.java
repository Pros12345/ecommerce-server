package eCommerse.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminOrderResponse {

	private Long orderId;

	private BigDecimal totalAmount;

	private String paymentMethod;

	private String orderStatus;

	private LocalDateTime orderDate;

	private AddressResponse address;

	private List<AdminOrderItemResponse> items;
}