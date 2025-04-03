package com.example.TakerManger.Model;

import com.example.TakerManger.Dto.ProductDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Product{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String brand;
    private BigDecimal price;
    private int inventory;
    private String description;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST}) // to delete the product
    @JoinColumn(name = "category_id")                                                      // without delete the category
    private Category category;


   @OneToOne(mappedBy = "product",cascade = CascadeType.ALL)
   private Image image ;

    private BigDecimal rating ;

    



}
