package com.example.commerce.service;

import com.example.commerce.domain.Product;
import com.example.commerce.exception.CustomException;
import com.example.commerce.exception.ErrorCode;
import com.example.commerce.model.ProductInfo;
import com.example.commerce.repository.ProductRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.Trie;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SearchService {

  private final ProductRepository productRepository;
  private final Trie trie;

  public void addAutocompleteKeyword(String keyword) {
    trie.put(keyword, null);
  }

  public List<String> getAutocompleteKeyword(String keyword) {
    return (List<String>) trie.prefixMap(keyword).keySet()
        .stream().limit(10).collect(Collectors.toList());
  }

  public void deleteAutocomplete(Long id) {
    Product product = productRepository.findById(id)
        .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));
    trie.remove(product.getProductName());
  }

  @Cacheable(key = "#pageable", value = "Product")
  public List<ProductInfo> searchProduct(Pageable pageable) {
    Page<Product> products = productRepository.findAll(pageable);
    return products.stream().map(product -> ProductInfo.builder()
        .productName(product.getProductName())
        .price(product.getPrice())
        .amount(product.getAmount())
        .maker(product.getMaker())
        .rating(product.getRating())
        .sales(product.getSales())
        .build()).collect(Collectors.toList());
  }

  @Cacheable(key = "#id", value = "Product")
  public ProductInfo getProductInfo(Long id) {
    Product product = productRepository.findById(id)
        .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

    return ProductInfo.builder()
        .productName(product.getProductName())
        .price(product.getPrice())
        .amount(product.getAmount())
        .maker(product.getMaker())
        .rating(product.getRating())
        .sales(product.getSales())
        .build();
  }
}
