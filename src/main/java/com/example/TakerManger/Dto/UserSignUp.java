package com.example.TakerManger.Dto;

import lombok.Data;

import java.util.Date;

@Data
public class UserSignUp {


    private Long id ;
    private String name ;
    private String userName ;
    private String phone ;
    private String email ;
    private String password;
}
