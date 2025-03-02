package com.example.TakerManger.Services.Product;

import com.example.TakerManger.Dto.*;
import com.example.TakerManger.Exception.ResourceNotFoundException;
import com.example.TakerManger.Model.Cart;
import com.example.TakerManger.Model.Category;
import com.example.TakerManger.Model.Image;
import com.example.TakerManger.Model.Product;
import com.example.TakerManger.Repository.CategoryRepository;
import com.example.TakerManger.Repository.ImageRepository;
import com.example.TakerManger.Repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ImageRepository imageRepository;
    private final ModelMapper modelMapper ;




    public Product addProduct(AddProductRequest request, MultipartFile file) {
        try {
            String catName = request.getCategory();
            Category category = categoryRepository.findByName(catName);
            if (category == null) {
                Category newCategory = new Category();
                newCategory.setName(catName);
                categoryRepository.save(newCategory);
                category = newCategory;
            }
            return createProduct(request, category, file);
        } catch (Exception e) {
            throw new RuntimeException("Error adding product", e);
        }
    }



    private Product createProduct(AddProductRequest request, Category category,MultipartFile file) throws IOException, SQLException {
        Product product = new Product();
        product.setName(request.getName());
        product.setBrand(request.getBrand());
        product.setPrice(request.getPrice());
        product.setInventory(request.getInventory());
        product.setDescription(request.getDescription());
        product.setCategory(category);

        Image image = new Image();
        image.setFileName(file.getOriginalFilename());
        image.setImage((new SerialBlob(file.getBytes())));
        image.setFileType(file.getContentType());
        image.setProduct(product);
        Image savedImage =  imageRepository.save(image);

        String downloadImage = "https://backendecommerace-bsgsgjeqe8gabda3.uaenorth-01.azurewebsites.net/api/v1/images/download/"+image.getId();
        savedImage.setDownloadUrl(downloadImage);

        product.setImage(savedImage);

        return productRepository.save(product);  // Save and return

    }






    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found!"));
    }

    @Override
    public void deleteProductById(Long id) {


            productRepository.findById(id).ifPresentOrElse(productRepository::delete,()->{
            throw new ResourceNotFoundException("the product with id " +id+" is not found ");
        });
    }

    @Override
    public Product updateProduct(ProductUpdateRequest request, Long productId) {
       Product currentProduct = productRepository.findById(productId).orElseThrow(()->new ResourceNotFoundException("prodcut not found"));
      Product updated = updateExistingProduct(currentProduct,request);
     return productRepository.save(updated);
    }

    private Product updateExistingProduct(Product existingProduct, ProductUpdateRequest request) {
        try {
            existingProduct.setName(request.getName());
            existingProduct.setPrice(request.getPrice());
            existingProduct.setInventory(request.getInventory());
            existingProduct.setBrand(request.getBrand());
            existingProduct.setDescription(request.getDescription());

            Category category = categoryRepository.findByName(request.getCategory());
            if (category!=null){
                existingProduct.setCategory(category);

            }else {
                Category newCategory = new Category();
                newCategory.setName(request.getCategory());
                categoryRepository.save(newCategory);
                existingProduct.setCategory(newCategory);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return existingProduct ;
    }

    @Override
   public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
    @Override
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategoryName(category);
    }


    @Override
    public List<Product> getProductsByBrand(String brand) {
        return productRepository.findByBrand(brand);
    }

    @Override
    public List<Product> getProductsByCategoryAndBrand(String category, String brand) {
        return productRepository.findByCategoryNameAndBrand(category, brand);
    }

    @Override
    public List<Product> getProductsByName(String name) {
        return productRepository.findByName(name);
    }

    @Override
    public List<Product> getProductsByBrandAndName(String brand, String name) {
        return productRepository.findByBrandAndName(brand, name);
    }

    @Override
    public Long countProductsByBrandAndName(String brand, String name) {
        return productRepository.countByBrandAndName(brand, name);
    }


    @Override
    public List<ProductDto> convertToDtoFun(List<Product> productList){

        return productList.stream().map(this::convertToDtoMethod).toList();


    }


@Override
    public ProductDto convertToDtoMethod(Product product){
        ProductDto productDto = modelMapper.map(product,ProductDto.class);

        Image singleImage = imageRepository.findByProductId(product.getId());
        ImageDto imageDto =   modelMapper.map(singleImage,ImageDto.class);

      productDto.setProductImage(imageDto);

      return productDto ;
    }

    @Override
    public CartDto convertCartDtoMethod(Cart cart){
        CartDto CartDto = modelMapper.map(cart,CartDto.class);

        return CartDto ;
    }


}
