package com.santojeet.sbproject.service;

import com.santojeet.sbproject.dto.ProductDto;
import com.santojeet.sbproject.entity.Product;
import com.santojeet.sbproject.exceptions.ResourceNotFoundException;
import com.santojeet.sbproject.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    public ProductService(ProductRepository productRepository, ModelMapper modelMapper) {
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
    }


    @Caching(
            put = { @CachePut(value = "PRODUCT_CACHE", key = "#result.id") },
            evict = { @CacheEvict(value = "PRODUCT_LIST_CACHE", allEntries = true) }
    )
    public ProductDto createProduct(ProductDto productDto) {
        log.info("Creating a new product with details Name: {} and Price: {}",productDto.getName(),productDto.getPrice());
        Product newProduct = modelMapper.map(productDto,Product.class);
        productRepository.save(newProduct);

        log.debug("New product created with details Name: {} and Price: {}",productDto.getName(),productDto.getPrice());
        return modelMapper.map(newProduct, ProductDto.class);
    }

    @Cacheable(value = "PRODUCT_CACHE" , key = "#productId")
    public ProductDto fetchProductById(String productId) {
        log.info("Fetching product with ID: {}",productId);
        Product product = productRepository.findById(productId)
                .orElseThrow(()->{
                    log.error("Product with product ID:{} not found",productId);
                    return new ResourceNotFoundException("Product not found with ID: "+productId);
                });

        log.debug("Fetched product details for product Name :{} Price:{}",product.getName(),product.getPrice());
        return modelMapper.map(product,ProductDto.class);
    }

    @Caching(
            evict = {
                    @CacheEvict(value = "PRODUCT_CACHE", key = "#productId"),
                    @CacheEvict(value = "PRODUCT_LIST_CACHE", allEntries = true)
            }
    )
    public ProductDto updateProduct(String productId,ProductDto productDto) {
        log.info("Fetching product with ID: {}",productId);
        Product exsistingProduct = productRepository.findById(productId)
                .orElseThrow(()->{
                    log.error("Product with product ID:{} not found",productId);
                    return new ResourceNotFoundException("Product not found with ID: "+productId);
                });

        exsistingProduct.setName(productDto.getName());
        exsistingProduct.setPrice(productDto.getPrice());

        Product updatedProduct = productRepository.save(exsistingProduct);

        log.debug("Updated product with details Name :{} Price:{}",productDto.getName(),productDto.getPrice());
        return modelMapper.map(updatedProduct, ProductDto.class);
    }

    @Caching(
            evict = {
                    @CacheEvict(value = "PRODUCT_CACHE", key = "#productId"),
                    @CacheEvict(value = "PRODUCT_LIST_CACHE", allEntries = true)
            }
    )
    public void deleteProduct(String productId) {
        log.info("Deleting product with ID: {}",productId);

        productRepository.findById(productId)
                .orElseThrow(()->{
                    log.error("Product with product ID:{} not found",productId);
                    return new ResourceNotFoundException("Product not found with ID: "+productId);
                });

        log.debug("Product deleted with ID :{}",productId);
        productRepository.deleteById(productId);
    }

    @Cacheable(value = "PRODUCT_LIST_CACHE")
    public List<ProductDto> fetchAllProducts() {
        log.info("Fetching all products from database");
        List<Product> productList = productRepository.findAll();

        log.debug("Returning all products present in database");
        return productList.stream()
                .map(product -> modelMapper.map(product,ProductDto.class))
                .collect(Collectors.toList());
    }
}
