package com.example.TakerManger.Dto;

import com.example.TakerManger.Model.Category;
import com.example.TakerManger.Model.Image;
import com.example.TakerManger.Model.Product;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ProductDto {

    private Long id;
    private String name;
    private String brand;
    private BigDecimal price;
    private int inventory;
    private String description;
    private categoryDto category;
    private ImageDto productImage ;


}
