package com.example.TakerManger.Controller;

import com.example.TakerManger.Dto.ProductDto;
import com.example.TakerManger.Exception.ResourceNotFoundException;
import com.example.TakerManger.Model.Users;
import com.example.TakerManger.Repository.ProductRepository;
import com.example.TakerManger.Repository.UserRepositiry;
import com.example.TakerManger.Response.ApiResponse;
import com.example.TakerManger.Services.Cart.CartServices;
import com.example.TakerManger.Services.User.UserServices;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@AllArgsConstructor
@RequestMapping("${api.prefix}")
public class UserController {

    private final UserRepositiry userRepositiry ;
    private final ProductRepository productRepository ;
    private final UserServices userServices;
    private final CartServices cartServices;

    @PostMapping("/save-product")
    public ResponseEntity<ApiResponse> savedProduct(@RequestParam Long userId, @RequestParam Long productId) {


        try {
            List <ProductDto> productDtoList = userServices.saveProduct(userId,productId);

            return ResponseEntity.ok(new ApiResponse("success",productDtoList));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(new ApiResponse("failed user or product is invalid",null));
        }


    }


    @GetMapping("/profile")
    public ResponseEntity<ApiResponse> userProfile(@RequestParam Long userId) {

        try {
            Users user = userRepositiry.findById(userId).orElseThrow(()->new ResourceNotFoundException("user is not found"));
            List<ProductDto> productDtoList = userServices.getAllSavedJobs(userId);
            return  ResponseEntity.ok(new ApiResponse("Success",productDtoList));
        } catch (ResourceNotFoundException e) {
           return ResponseEntity.status(404).body(new ApiResponse("failed",null));
        }

    }

    @PostMapping("/add-to-cart/{userId}/{productId}")
    public ResponseEntity<ApiResponse>addProductToCart(@PathVariable Long userId , @PathVariable Long productId ){
        try {
            return ResponseEntity.ok().body( new ApiResponse("Added Successfully",cartServices.addProductToCart(userId,productId)));
        } catch (Exception e) {
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body( new ApiResponse(e.getMessage(),null));
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse> getCart(@PathVariable Long userId) {
        try {

            return (ResponseEntity<ApiResponse>) cartServices.getUserCart(userId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PutMapping("/{userId}/increase/{cartItemId}")
    public ResponseEntity<ApiResponse> increaseProductQuantity(@PathVariable Long userId, @PathVariable Long cartItemId) {
        try {
            cartServices.increaseProductQuantity(userId, cartItemId);
            return ResponseEntity.ok(new ApiResponse("Product quantity increased successfully", null));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ApiResponse("Error: " + e.getMessage(), null));
        }
    }

    @PutMapping("/{userId}/decrease/{cartItemId}")
    public ResponseEntity<ApiResponse> decreaseProductQuantity(@PathVariable Long userId, @PathVariable Long cartItemId) {
        try {
            cartServices.decreaseProductQuantity(userId, cartItemId);
            return ResponseEntity.ok(new ApiResponse("Product quantity decreased successfully", null));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ApiResponse("Error: " + e.getMessage(), null));
        }
    }


    @DeleteMapping("/{userId}/delete/{cartItemId}")
    public ResponseEntity<ApiResponse> deleteCartItem(@PathVariable Long userId, @PathVariable Long cartItemId) {
        try {
            cartServices.deleteCartItem(userId, cartItemId);
            return ResponseEntity.ok(new ApiResponse("Product deleted successfully", null));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ApiResponse("Error: " + e.getMessage(), null));
        }
    }


    }
