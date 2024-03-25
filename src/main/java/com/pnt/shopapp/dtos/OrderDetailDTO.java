package com.pnt.shopapp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDetailDTO {
    @Min(value = 1, message = "Order's Id must be >0")
    @JsonProperty("order_id")
    private Long orderId;
    @JsonProperty("product_id")
    private Long productId;
    @Min(value=0,message = "Số lượng phải lớn hơn 0")
    private int quantity;
    @Min(value = 0, message = "Price of product must be >=0")
    private Long price;
    @Min(value = 0, message = "Total money must be >=0")
    @JsonProperty("total_money")
    private float totalMoney;
    private String color;
}
