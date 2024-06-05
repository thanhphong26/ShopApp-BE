package com.pnt.shopapp.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pnt.shopapp.models.OrderDetail;
import jakarta.persistence.*;
import lombok.*;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDetailResponse {
    private Long id;
    @JsonProperty("order_id")
    private Long orderId;
    @JoinColumn(name = "product_id")
    private Long productId;
    @Column(name = "quantity", nullable = false)
    private Integer quantity;
    @Column(name = "price", nullable = false)
    private Float price;
    @Column(name = "total_money", nullable = false)
    private Float totalMoney;
    @Column(name="color", length = 100)
    private String color;
    public static OrderDetailResponse fromOrderDetailResponse(OrderDetail orderDetail){
        return OrderDetailResponse
                .builder()
                .id(orderDetail.getId())
                .orderId(orderDetail.getOrder().getId())
                .productId(orderDetail.getProduct().getId())
                .price(orderDetail.getPrice())
                .quantity(orderDetail.getQuantity())
                .totalMoney(orderDetail.getTotalMoney())
                .color(orderDetail.getColor())
                .build();
    }
}
