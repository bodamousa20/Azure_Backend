package com.example.TakerManger.Repository;

import com.example.TakerManger.Model.Category;
import com.example.TakerManger.Model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category,Long> {


    Category findByName(String name);




    boolean existsByName(String name);

}
