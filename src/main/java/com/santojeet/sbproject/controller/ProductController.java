package com.santojeet.sbproject.controller;

import com.santojeet.sbproject.dto.ProductDto;
import com.santojeet.sbproject.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public ProductDto createProduct(@Valid @RequestBody ProductDto productDto) {
        return productService.createProduct(productDto);
    }

    @GetMapping
    @ResponseStatus(value = HttpStatus.OK)
    public List<ProductDto> getAllProduct() {
        return productService.fetchAllProducts();
    }

    @GetMapping("/{productId}")
    @ResponseStatus(value = HttpStatus.OK)
    public ProductDto getProductById(@PathVariable String productId) {
        return productService.fetchProductById(productId);
    }

    @PutMapping("/{productId}")
    @ResponseStatus(value = HttpStatus.OK)
    public ProductDto updateProduct(@PathVariable String productId,@Valid @RequestBody ProductDto productDto) {
        return productService.updateProduct(productId,productDto);
    }

    @DeleteMapping("/{productId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable String productId) {
        productService.deleteProduct(productId);
    }
}
