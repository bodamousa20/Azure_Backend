package com.example.TakerManger.Repository;

import com.example.TakerManger.Model.SavedProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface savedJobRepository extends JpaRepository<SavedProduct,Long> {

   Boolean existsByUsersIdAndProductId(Long usersId, Long productID);

    List<SavedProduct> findByUsersId(Long userId);
}
