package com.example.TakerManger.Dto;


import com.example.TakerManger.Model.Category;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AddProductRequest {
    private Long id;
    private String name;
    private String brand;
    private BigDecimal price;
    private int inventory;
    private String description;
    private String category;
    private BigDecimal rating;


}