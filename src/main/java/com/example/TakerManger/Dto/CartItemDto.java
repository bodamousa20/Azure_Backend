package com.example.TakerManger.Dto;

import com.example.TakerManger.Model.Cart;
import com.example.TakerManger.Model.Product;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CartItemDto {
    private Long id ;
    private ProductDto product ;
    private BigDecimal unitPrice ;
    private Long quantity = 1L;
}
