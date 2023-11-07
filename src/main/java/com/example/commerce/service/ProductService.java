package com.example.commerce.service;

import com.example.commerce.domain.Product;
import com.example.commerce.domain.User;
import com.example.commerce.exception.CustomException;
import com.example.commerce.exception.ErrorCode;
import com.example.commerce.model.ModifyProduct;
import com.example.commerce.model.RegisterProduct;
import com.example.commerce.repository.ProductRepository;
import com.example.commerce.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

  private final ProductRepository productRepository;
  private final UserRepository userRepository;

  public RegisterProduct.Response registerProduct(
      RegisterProduct.Request registerForm, String username) {
    productNameDuplicatedCheck(registerForm.getProductName());

    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

    Product product = productRepository.save(Product.builder()
        .user(user)
        .productName(registerForm.getProductName())
        .price(registerForm.getPrice())
        .amount(registerForm.getAmount())
        .maker(registerForm.getMaker())
        .sales(0L)
        .build());

    return RegisterProduct.Response.builder()
        .productId(product.getId())
        .productName(product.getProductName())
        .build();
  }

  private void productNameDuplicatedCheck(String productName) {
    if (productRepository.existsByProductName(productName)) {
      throw new CustomException(ErrorCode.ALREADY_EXISTS_PRODUCT_NAME);
    }
  }

  public void modifyProduct(ModifyProduct.Request modifyForm, Long id, String username) {
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

    Product product = productRepository.findByIdAndUser(id, user)
        .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

    Product modifiedProduct = modifyProductAttribute(modifyForm, product);
    productRepository.save(modifiedProduct);
  }

  private Product modifyProductAttribute(ModifyProduct.Request modifyForm, Product product) {
    product.setProductName(modifyForm.getProductName());
    product.setPrice(modifyForm.getPrice());
    product.setAmount(modifyForm.getAmount());
    product.setMaker(modifyForm.getMaker());

    return product;
  }
}
