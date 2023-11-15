package com.example.commerce.service;

import com.example.commerce.domain.Cart;
import com.example.commerce.domain.Product;
import com.example.commerce.domain.User;
import com.example.commerce.exception.CustomException;
import com.example.commerce.exception.ErrorCode;
import com.example.commerce.model.CartInfo;
import com.example.commerce.repository.CartRepository;
import com.example.commerce.repository.ProductRepository;
import com.example.commerce.repository.UserRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartService {

  private final CartRepository cartRepository;
  private final ProductRepository productRepository;
  private final UserRepository userRepository;

  public void addProduct(Long productId, String username) {
    Product product = productRepository.findById(productId)
        .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

    Cart cart = Cart.builder()
        .user(user)
        .product(product)
        .amount(1L)
        .build();
    cartRepository.save(cart);
  }

  public List<CartInfo> getProducts(String username) {
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

    List<Cart> carts = cartRepository.findByUser(user)
        .orElseThrow(() -> new CustomException(ErrorCode.CART_NOT_FOUND));

    return carts.stream().map(cart -> CartInfo.builder()
        .productName(cart.getProduct().getProductName())
        .price(cart.getProduct().getPrice())
        .amount(cart.getAmount())
        .build()).collect(Collectors.toList());
  }
}
