package com.example.commerce.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.example.commerce.domain.Product;
import com.example.commerce.model.ProductInfo;
import com.example.commerce.repository.ProductRepository;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.collections4.Trie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class SearchServiceTest {

  @Mock
  private ProductRepository productRepository;

  @Mock
  private Trie trie;

  @InjectMocks
  private SearchService searchService;

  @Test
  @DisplayName("상품 불러오기 성공 테스트")
  void searchProductSuccess() {
    //given
    Product apple = Product.builder()
        .productName("사과")
        .price(1000L)
        .amount(100L)
        .maker("의성")
        .rating(null)
        .sales(0L)
        .build();
    Product pear = Product.builder()
        .productName("배")
        .price(1000L)
        .amount(100L)
        .maker("나주")
        .rating(null)
        .sales(0L)
        .build();
    List<Product> products = new ArrayList<>();
    products.add(apple);
    products.add(pear);

    Pageable pageable = PageRequest.of(0, 10);
    Page<Product> productsPage = new PageImpl<>(products);

    given(productRepository.findAll((Pageable) any()))
        .willReturn(productsPage);
    //when
    List<ProductInfo> response = searchService.searchProduct(pageable);
    //then
    assertEquals("사과", response.get(0).getProductName());
    assertEquals("배", response.get(1).getProductName());
  }
}