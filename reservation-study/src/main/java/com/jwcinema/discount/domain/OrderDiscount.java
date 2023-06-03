package com.jwcinema.discount.domain;

import com.jwcinema.common.InvalidParameterException;
import lombok.Getter;
import org.springframework.util.ObjectUtils;

import java.time.LocalDate;

@Getter
public class OrderDiscount {

    private DiscountId id;
    private DiscountPolicy policy;

    public long calculateDiscountPrice(long screenPrice){
        return policy.apply(screenPrice);
    }

    public static OrderDiscount createDiscount(DiscountId id, DiscountPolicy policy){
        validate(id, policy);
        return new OrderDiscount(id,policy);
    }

    public OrderDiscount(DiscountId id, DiscountPolicy policy) {
        this.id = id;
        this.policy = policy;
    }

    private static void validate(DiscountId id, DiscountPolicy policy){
        if(ObjectUtils.isEmpty(id.getDate())){
            throw new InvalidParameterException("date는 필수값입니다.");
        }
        if(id.getDate().isBefore(LocalDate.now())){
            throw new InvalidParameterException("date는 과거로 등록할 수 없습니다.");
        }
        if(ObjectUtils.isEmpty(id.getDayOfOrder())){
            throw new InvalidParameterException("dayOfOrder 값은 필수잆니다.");
        }
        if(id.getDayOfOrder() <= 0){
            throw new InvalidParameterException("dayOfOrder 값은 0 또는 음수가 될 수 없습니다.");
        }
        if(ObjectUtils.isEmpty(policy)){
            throw new InvalidParameterException("discount 값은 필수값입니다.");
        }
    }

    public OrderDiscountEntity toDiscountEntity() {
        return OrderDiscountEntity.builder()
                .discountDate(id.getDate())
                .dayOfOrder(id.getDayOfOrder())
                .build();
    }

    public DiscountPolicyEntity toPolicyEntity(Long discountId) {
        return DiscountPolicyEntity.builder()
                .discountId(discountId)
                .type(this.getPolicy().getType())
                .rate(this.getPolicy().getRate())
                .price(this.getPolicy().getPrice())
                .build();
    }
}
