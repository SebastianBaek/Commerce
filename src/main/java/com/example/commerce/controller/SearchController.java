package com.example.commerce.controller;

import com.example.commerce.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/search")
public class SearchController {

  private final SearchService searchService;

  @GetMapping("/autocomplete")
  public ResponseEntity<?> autocomplete(@RequestParam String keyword) {
    return ResponseEntity.ok(searchService.getAutocompleteKeyword(keyword));
  }
}
