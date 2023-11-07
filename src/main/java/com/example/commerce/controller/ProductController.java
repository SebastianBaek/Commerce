package com.example.commerce.controller;

import com.example.commerce.model.ModifyProduct;
import com.example.commerce.model.RegisterProduct;
import com.example.commerce.service.ProductService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {

  private final ProductService productService;

  @PostMapping("/register")
  @PreAuthorize("hasRole('SELLER')")
  public ResponseEntity<?> registerProduct(
      @RequestBody @Valid RegisterProduct.Request registerForm, Authentication authentication) {
    return ResponseEntity.ok(
        productService.registerProduct(registerForm, authentication.getName()));
  }

  @PutMapping("/modify/{id}")
  @PreAuthorize("hasRole('SELLER')")
  public ResponseEntity<?> modifyProduct(
      @RequestBody @Valid ModifyProduct.Request modifyForm,
      @PathVariable Long id,
      Authentication authentication) {
    productService.modifyProduct(modifyForm, id, authentication.getName());
    return ResponseEntity.ok().build();
  }

  @GetMapping("/getAll")
  public ResponseEntity<?> getAllSellerProduct(Authentication authentication) {
    return ResponseEntity.ok(productService.getAllSellerProducts(authentication.getName()));
  }
}
