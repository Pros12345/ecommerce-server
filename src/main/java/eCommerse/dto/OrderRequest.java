package eCommerse.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderRequest {

	@NotNull
	private Long addressId;

	@NotEmpty
	@Valid
	private List<OrderItemRequest> items;

	@NotNull
	private String paymentMethod;
}