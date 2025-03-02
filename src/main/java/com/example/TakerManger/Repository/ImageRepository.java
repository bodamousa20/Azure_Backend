package com.example.TakerManger.Repository;

import com.example.TakerManger.Model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image,Long> {


    Image findByProductId(Long id);
}
