package com.example.TakerManger.Services.Cart;

import com.example.TakerManger.Dto.CartDto;
import com.example.TakerManger.Exception.ResourceNotFoundException;
import com.example.TakerManger.Model.Cart;
import com.example.TakerManger.Model.CartItem;
import com.example.TakerManger.Model.Product;
import com.example.TakerManger.Model.Users;
import com.example.TakerManger.Repository.CartItemRepository;
import com.example.TakerManger.Repository.CartRepository;
import com.example.TakerManger.Repository.ProductRepository;
import com.example.TakerManger.Repository.UserRepositiry;
import com.example.TakerManger.Response.ApiResponse;
import com.example.TakerManger.Services.Product.ProductService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor

public class CartServices implements ICartServices {

    private final CartRepository cartRepository;

    private final UserRepositiry userRepositiry;
    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;

    private final ProductService productService;

    @Override
    @Transactional
    public CartDto addProductToCart(Long userId, Long productId) {
      Users currentUser =  userRepositiry.findById(userId).orElseThrow(()->new ResourceNotFoundException("The current user is not found "));
      Product product = productRepository.findById(productId).orElseThrow(()->new ResourceNotFoundException("the job is not found"));

        Cart cart = currentUser.getCart();

        CartDto cartDto = null;
        try {
            if(cart == null){
                cart = new Cart();
                cart.setCartItemList(new ArrayList<>());
                currentUser.setCart(cart);
                cartRepository.save(cart);

            }
            CartItem cartItem = new CartItem() ;


            cartItem.setProduct(product);
            //price of product
            cartItem.setUnitPrice(product.getPrice());

            cartItem.setCart(cart);

            cart.getCartItemList().add(cartItem);
            cartItemRepository.save(cartItem);
            cartRepository.save(cart);
            userRepositiry.save(currentUser);

            calculateCartTotals(userId);
            cartDto = productService.convertCartDtoMethod(cart);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return cartDto ;
    }


    @Override
    @Transactional
    public void calculateCartTotals(Long userId) {

        Users user = userRepositiry.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid user ID"));

        Cart cart = user.getCart();
        cart.setTotalPrice( BigDecimal.ZERO ) ;

        BigDecimal totalPrice = cart.getTotalPrice();

        if (cart == null || cart.getCartItemList().isEmpty()) {
            throw new ResourceNotFoundException("Cart is empty");
        }
                List<CartItem> cartItemList =  cart.getCartItemList() ;

            for(CartItem item : cartItemList) {
                totalPrice = totalPrice.add(item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
            }
            cart.setTotalPrice(totalPrice);
            cartRepository.save(cart);
    }

    @Override
    @Transactional
    public void increaseProductQuantity(Long userId, Long cartItemId) {

        Users user =      userRepositiry.findById(userId)
                .orElseThrow(()->new ResourceNotFoundException("the user id is invalid"));

        Cart cart =        user.getCart();

        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found"));

        List <CartItem> cartItemList =  cart.getCartItemList();

        for(CartItem cartItem1 : cartItemList){
            if(cartItem1.getId().equals(cartItem.getId())){
                //operation
                    cartItem.setQuantity( cartItem.getQuantity()  + 1);
                    cartItemRepository.save(cartItem);
            }
        }


        calculateCartTotals(userId);

    }



    @Override
    @Transactional
    public void decreaseProductQuantity(Long userId, Long cartItemId) {
        Users user = userRepositiry.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("The user ID is invalid"));

        Cart cart = user.getCart();

        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found"));

        if (cartItem.getQuantity() > 1) {
            // Just decrease the quantity
            cartItem.setQuantity(cartItem.getQuantity() - 1);
            calculateCartTotals(userId);
            cartItemRepository.save(cartItem);
        } else {
            // Delete the item if it's the last one
            deleteCartItem(userId, cartItemId);
        }

    }
    @Override
    @Transactional
    public void deleteCartItem(Long userId, Long cartItemId) {
        Users user = userRepositiry.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("The user ID is invalid"));

        Cart cart = user.getCart();

        CartItem cartItem = cart.getCartItemList().stream()
                .filter(item -> item.getId().equals(cartItemId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found"));

      if( cart.getCartItemList().size()==1 ){
          cart.getCartItemList().clear();

      }

        // Remove item from cart
        cart.getCartItemList().remove(cartItem);
        cartItemRepository.delete(cartItem);
        System.out.println(cart.getCartItemList());

        if (cart.getCartItemList().isEmpty()) {
            cart.setTotalPrice(BigDecimal.ZERO);
        }
        calculateCartTotals(userId);
        cartRepository.save(cart);
        calculateCartTotals(userId);
    }


    @Override
    public ResponseEntity<?>getUserCart(Long userId){
      Users user =  userRepositiry.findById(userId).orElseThrow(()->new ResourceNotFoundException("User Not found "));

     Cart cart =  user.getCart();

     if(cart.getCartItemList().isEmpty()){

         return ResponseEntity.ok( new ApiResponse("The cart is empty ",null));
     }
     else{
        return ResponseEntity.ok(new ApiResponse("success",productService.convertCartDtoMethod(cart)));

     }
    }


}
