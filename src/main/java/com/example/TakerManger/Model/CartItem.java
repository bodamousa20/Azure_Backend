package com.example.TakerManger.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.logging.log4j.util.Lazy;

import java.math.BigDecimal;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;

    private BigDecimal unitPrice ;

    private Long quantity = 1L;

    @ManyToOne
    @JoinColumn(name = "card_id")
    private Cart Cart ;

    @ManyToOne(fetch = FetchType.LAZY,cascade = {CascadeType.MERGE,CascadeType.PERSIST,CascadeType.DETACH})
    @JoinColumn(name = "product_id")
    private Product product ;

}
