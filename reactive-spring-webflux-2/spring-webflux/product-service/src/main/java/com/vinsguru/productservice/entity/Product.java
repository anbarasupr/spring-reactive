package com.vinsguru.productservice.entity;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;

@Data
public class Product {

    @Id
    private String id;
    private String description;
    private Integer price;

}
