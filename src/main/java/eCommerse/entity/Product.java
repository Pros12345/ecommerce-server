package eCommerse.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name = "product")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ToString.Include
    private int productId;

    @NotNull
    private String productName;

    @NotNull
    private String productDescription;

    @NotNull
    private double price;

    @NotNull
    private int stockQuantity;
}
