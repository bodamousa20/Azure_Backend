package com.wuzzafni.backend.Repositary;

import com.wuzzafni.backend.Model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

    public interface ImageRepository extends JpaRepository<Image,Long> {

}
