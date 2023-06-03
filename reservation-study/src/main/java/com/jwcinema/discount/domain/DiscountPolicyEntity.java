package com.jwcinema.discount.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@SequenceGenerator(
        name = "DISCOUNT_POLICY_SEQ_GENERATOR",
        sequenceName = "DISCOUNT_POLICY_SEQ",
        allocationSize = 1
)
@Getter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "DISCOUNT_POLICY")
public class DiscountPolicyEntity {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DISCOUNT_POLICY_SEQ_GENERATOR")
    private Long id;
//    @EmbeddedId
//    private DiscountId discountId;
    @Column(name = "discount_id")
    private Long discountId;
    @Column(name = "type")
    private String type;
    @Column(name = "rate")
    private Integer rate;
    @Column(name = "price")
    private Long price;

    @OneToOne(mappedBy = "policy")
    private OrderDiscountEntity orderDiscount;
}
