package com.pnt.shopapp.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
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
    private LocalDate orderDate;
    @Column(name = "status")
    private String oderStatus;
    @Column(name = "total_money")
    private Float totalMoney;
    @Column(name="shipping_method", length = 100)
    private String shippingMethod;
    @Column(name="shipping_address", length=100)
    private String shippingAddress;
    @Column(name="shipping_date")
    private LocalDate shippingDate;
    @Column(name="active")
    private Boolean active;//thuộc về admin
    @Column(name="tracking_number", length=100)
    private String trackingNumber;
    @Column(name="payment_method", length=100)
    private String paymentMethod;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    @JsonProperty("order_details")
    private List<OrderDetail> orderDetails;
}
