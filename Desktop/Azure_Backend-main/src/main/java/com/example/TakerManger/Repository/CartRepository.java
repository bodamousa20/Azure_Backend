package com.example.TakerManger.Repository;

import com.example.TakerManger.Model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart,Long> {
}
