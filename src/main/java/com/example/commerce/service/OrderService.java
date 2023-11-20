package com.example.commerce.service;

import com.example.commerce.domain.Coupon;
import com.example.commerce.domain.Order;
import com.example.commerce.domain.Product;
import com.example.commerce.domain.User;
import com.example.commerce.exception.CustomException;
import com.example.commerce.exception.ErrorCode;
import com.example.commerce.model.OrderProduct;
import com.example.commerce.model.OrderProduct.Response;
import com.example.commerce.repository.CouponRepository;
import com.example.commerce.repository.OrderRepository;
import com.example.commerce.repository.ProductRepository;
import com.example.commerce.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

  private final OrderRepository orderRepository;
  private final ProductRepository productRepository;
  private final UserRepository userRepository;
  private final CouponRepository couponRepository;

  public Response orderProduct(OrderProduct.Request request, String username) {
    Product product = productRepository.findById(request.getProductId())
        .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

    if (request.getCouponId() != null) {
      Coupon coupon = couponRepository.findByUserAndId(user, request.getCouponId())
          .orElseThrow(() -> new CustomException(ErrorCode.COUPON_NOT_FOUND));
      product = discount(product, coupon);
    }

    Order order = orderRepository.save(Order.builder()
        .user(user)
        .product(product)
        .amount(request.getAmount())
        .address(request.getAddress())
        .build());

    return OrderProduct.Response.builder()
        .orderId(order.getId())
        .productName(product.getProductName())
        .sum(product.getPrice())
        .amount(order.getAmount())
        .address(order.getAddress())
        .build();
  }

  private Product discount(Product product, Coupon coupon) {
    Long price = product.getPrice();

    if (coupon.getRate() != null) {
      product.setPrice(price * (100 - coupon.getRate()));
    }

    if (coupon.getSaving() != null) {
      product.setPrice(Math.max(0, price - coupon.getSaving()));
    }

    return product;
  }
}
