package com.example.TakerManger.Services.User;

import com.example.TakerManger.Dto.ProductDto;
import com.example.TakerManger.Dto.UserDto;
import com.example.TakerManger.Dto.UserSignUp;
import com.example.TakerManger.Exception.ResourceNotFoundException;
import com.example.TakerManger.Model.Cart;
import com.example.TakerManger.Model.Product;
import com.example.TakerManger.Model.SavedProduct;
import com.example.TakerManger.Model.Users;
import com.example.TakerManger.Repository.ProductRepository;
import com.example.TakerManger.Repository.UserRepositiry;
import com.example.TakerManger.Repository.savedJobRepository;
import com.example.TakerManger.Services.Product.ProductService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServices implements IUserServices {


    private final PasswordEncoder passwordEncoder ;



    private final UserRepositiry userRepositiry;
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper ;
    private final savedJobRepository savedJobRepository ;
    private final ProductService productService ;




    @Override
    public Users getUserProfileById(Long id) {
        return userRepositiry.findById(id).orElseThrow(() -> new ResourceNotFoundException("User with this id is not exist"));
    }

    @Override
    @Transactional
    public Users createUser(UserSignUp userDto) {
        Users newUser = new Users();
        Cart newCart = new Cart();



        try {
            newUser.setName(userDto.getName());
            newUser.setCart(newCart);
            newUser.setEmail(userDto.getEmail());
            newUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
            newUser.setName(userDto.getName());
            newUser.setPhone(userDto.getPhone());
            newUser.setUsername(userDto.getUserName());
            userRepositiry.save(newUser);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return newUser;


    }
    @Override
    public List<ProductDto> saveProduct(Long userId, Long productID) {
        Users user = userRepositiry.findById(userId).orElseThrow(()->new ResourceNotFoundException("user is not found"));
        Product product = productRepository.findById(productID).orElseThrow(()->new ResourceNotFoundException("product not found"));
        boolean isJobAlreadySaved = savedJobRepository.existsByUsersIdAndProductId(userId, productID);
        if (isJobAlreadySaved) {
            throw new IllegalStateException("Job is already saved.");
        }

        SavedProduct savedJob = new SavedProduct();
        savedJob.setUsers(user);
        savedJob.setProduct(product);
        savedJobRepository.save(savedJob);
        return getAllSavedJobs(userId);

    }

    public List<ProductDto> getAllSavedJobs(Long userId) {


     List<SavedProduct> SavedProduct  = savedJobRepository.findByUsersId(userId) ;

     List<Product>  pr = SavedProduct.stream().map(product -> product.getProduct()).toList();


       return productService.convertToDtoFun(pr);


    }

    // Check if Job is Saved
    public boolean isJobSaved(Long userId, Long productId) {
        return savedJobRepository.existsByUsersIdAndProductId(userId, productId);
    }



    @Override
    public UserDto convertToDtoMethoduser(Users user){
        UserDto userDto = modelMapper.map(user, UserDto.class);
        return userDto ;
    }

}
