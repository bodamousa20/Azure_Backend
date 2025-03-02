package com.example.TakerManger.Services.Cart;

import com.example.TakerManger.Dto.CartDto;
import com.example.TakerManger.Model.Cart;
import com.example.TakerManger.Model.CartItem;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


public interface ICartServices {

     CartDto addProductToCart(Long userId, Long productId);

     void increaseProductQuantity(Long userId,Long cartItemId);
     void decreaseProductQuantity(Long userId,Long cartItemId);

     void calculateCartTotals(Long userId);

     void deleteCartItem(Long userId, Long cartItemId);
      ResponseEntity<?>getUserCart(Long userId);



}
