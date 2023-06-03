package com.jwcinema.discount.infra;

import com.jwcinema.discount.domain.DiscountPolicyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiscountPolicyEntityRepository extends JpaRepository<DiscountPolicyEntity, Long> {
}
