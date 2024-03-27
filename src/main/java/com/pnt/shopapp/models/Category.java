package com.pnt.shopapp.models;

import jakarta.persistence.*;
import lombok.*;

@Table(name = "categories")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;
    @Column(name = "name", length = 100)
    private String name;
}
