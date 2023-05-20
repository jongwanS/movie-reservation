package com.jwcinema.discount.infra;

import com.jwcinema.discount.domain.DiscountPolicyEntity;
import com.jwcinema.discount.domain.OrderDiscount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface DiscountPolicyEntityRepository extends JpaRepository<DiscountPolicyEntity, Long> {
}
