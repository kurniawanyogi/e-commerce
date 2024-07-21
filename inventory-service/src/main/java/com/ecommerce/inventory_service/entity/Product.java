package com.ecommerce.inventory_service.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;


@Table(name = "PRODUCT")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Builder
public class Product {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "PRODUCT_NAME")
    private String productName;
    @Column(name = "PRICE")
    private long price;
    @Column(name = "QUANTITY")
    private long quantity;


    @JsonIgnore
    @Column(name = "VERSION")
    @Version
    private long version;
    @JsonIgnore
    @Column(name = "CREATED_BY")
    private String createdBy;
    @JsonIgnore
    @Column(name = "LAST_UPDATED_BY")
    private String lastUpdatedBy;
    @JsonIgnore
    @Column(name = "CREATED_DATE")
    private Timestamp createdDate;
    @JsonIgnore
    @Column(name = "LAST_UPDATED_DATE")
    private Timestamp lastUpdatedDate;
}