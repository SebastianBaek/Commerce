package com.example.commerce.controller;

import com.example.commerce.model.ModifyProduct;
import com.example.commerce.model.RegisterProduct;
import com.example.commerce.service.ProductService;
import com.example.commerce.service.SearchService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
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
  private final SearchService searchService;

  @PostMapping("/register")
  @PreAuthorize("hasRole('SELLER')")
  public ResponseEntity<?> registerProduct(
      @RequestBody @Valid RegisterProduct.Request registerForm, Authentication authentication) {
    searchService.addAutocompleteKeyword(registerForm.getProductName());
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
  @PreAuthorize("hasRole('SELLER')")
  public ResponseEntity<?> getAllSellerProduct(Authentication authentication) {
    return ResponseEntity.ok(productService.getAllSellerProducts(authentication.getName()));
  }

  @DeleteMapping("/remove/{id}")
  @PreAuthorize("hasRole('SELLER')")
  public ResponseEntity<?> removeProduct(@PathVariable Long id, Authentication authentication) {
    searchService.deleteAutocomplete(id);
    productService.removeProduct(id, authentication.getName());
    return ResponseEntity.ok().build();
  }
}
