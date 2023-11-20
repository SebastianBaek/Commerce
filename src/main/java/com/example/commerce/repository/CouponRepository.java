package com.example.commerce.repository;

import com.example.commerce.domain.Coupon;
import com.example.commerce.domain.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponRepository extends JpaRepository<Coupon, Long> {

  Optional<List<Coupon>> findByUser(User user);

  Optional<Coupon> findByUserAndId(User user, Long id);
}
