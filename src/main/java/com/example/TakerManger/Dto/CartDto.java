package com.example.TakerManger.Dto;

import com.example.TakerManger.Model.CartItem;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CartDto {
    private Long id ;

    List<CartItemDto> cartItemList ;

    private BigDecimal totalPrice ;
}
