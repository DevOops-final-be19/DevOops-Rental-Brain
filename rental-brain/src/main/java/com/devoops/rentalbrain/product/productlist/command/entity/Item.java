package com.devoops.rentalbrain.product.productlist.command.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "item")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Item {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "serial_num")
    private String serialNum;

    @Column(name = "monthly_price")
    private int monthlyPrice;

    @Column(name = "status")
    private String status;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "last_inspect_date")
    private LocalDateTime lastInspectDate;

    @Column(name = "sales")
    private int sales;

    @Column(name = "repair_cost")
    private int repairCost;

    @Column(name = "category_id")
    private long categoryId;
}

