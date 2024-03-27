package com.pnt.shopapp.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.Date;

@Table(name = "orders")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @Column(name = "fullname", length = 100)
    private String fullName;
    @Column(name = "email", length = 100)
    private String email;
    @Column(name = "phone_number", length = 10, nullable = false)
    private String phoneNumber;
    @Column(name = "address", length = 100)
    private String address;
    @Column(name = "note")
    private String note;

    @Column(name = "order_date")
    private Timestamp orderDate;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private OrderStatus oderStatus;
    @Column(name = "total_money")
    private Float totalMoney;
    @Column(name="shipping_method", length = 100)
    private String shippingMethod;
    @Column(name="shipping_address", length=100)
    private String shippingAddress;
    @Column(name="shipping_date")
    private Date shippingDate;
    @Column(name="active")
    private byte active;
    @Column(name="tracking_number", length=100)
    private String trackingNumber;
    @Column(name="payment_method", length=100)
    private String paymentMethod;
}
