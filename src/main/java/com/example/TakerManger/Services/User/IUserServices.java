package com.example.TakerManger.Services.User;

import com.example.TakerManger.Dto.ProductDto;
import com.example.TakerManger.Dto.UserDto;
import com.example.TakerManger.Dto.UserSignUp;
import com.example.TakerManger.Model.Users;

import java.util.List;

public interface IUserServices {

     Users getUserProfileById(Long id );



    Users createUser(UserSignUp userDto);


    List<ProductDto> saveProduct (Long productID, Long userId);

    UserDto convertToDtoMethoduser(Users user);
}
