package com.example.TakerManger.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;

    private String name ;
    private String username ;

   @Size(min = 11)
    private String phone ;

    @Email(message = "email is not valid")
    @NotEmpty
    @Column(unique = true, nullable = false)
    private String email ;

    @Size(min = 7)
    private String password ;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_cart_id")
    private Cart cart ;




}
