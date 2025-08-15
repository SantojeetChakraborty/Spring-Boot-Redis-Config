package com.santojeet.sbproject.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
@Data
public class ProductDto {
    private String id;
    @NotBlank(message = "Product name cannot be blank")
    private String name;
    @NotNull(message = "Product price cannot be blank")
    @Positive(message = "Price must be greater than Zero")
    private BigDecimal price;
}
