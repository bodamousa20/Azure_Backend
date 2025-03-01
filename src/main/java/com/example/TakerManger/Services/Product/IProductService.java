package com.example.TakerManger.Services.Product;


import com.example.TakerManger.Dto.*;
import com.example.TakerManger.Model.Cart;
import com.example.TakerManger.Model.Product;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IProductService {
    Product addProduct(AddProductRequest product, MultipartFile file);
    Product getProductById(Long id);
    void deleteProductById(Long id);
    Product updateProduct(ProductUpdateRequest product, Long productId);
    List<Product> getAllProducts();
    List<Product> getProductsByCategory(String category);
    List<Product> getProductsByBrand(String brand);
    List<Product> getProductsByCategoryAndBrand(String category, String brand);
    List<Product> getProductsByName(String name);
    List<Product> getProductsByBrandAndName(String category, String name);
    Long countProductsByBrandAndName(String brand, String name);



    List<ProductDto> convertToDtoFun(List<Product> productList);

    ProductDto convertToDtoMethod(Product product);
     CartDto convertCartDtoMethod(Cart cart);


}
