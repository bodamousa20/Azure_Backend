package com.example.TakerManger.Controller;

import com.example.TakerManger.Dto.AddProductRequest;
import com.example.TakerManger.Dto.ProductDto;
import com.example.TakerManger.Dto.ProductUpdateRequest;
import com.example.TakerManger.Exception.ResourceNotFoundException;
import com.example.TakerManger.Model.Product;
import com.example.TakerManger.Response.ApiResponse;
import com.example.TakerManger.Services.Product.ProductService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.logging.ErrorManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@AllArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping("${api.prefix}/product")
public class ProductController {

    private final ProductService productService;
    @PostMapping("/add-product")
    public ResponseEntity<ApiResponse> addProduct( @ModelAttribute AddProductRequest request, @RequestParam("image") MultipartFile image) {


        try {
            Product p = productService.addProduct(request, image);
            System.out.println("product name "+ p.getName() + "prodcut id "+p.getImage().getId());
            ProductDto productDto =    productService.convertToDtoMethod(p);
            return ResponseEntity.ok(new ApiResponse("Product Added Successfully", productDto));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ApiResponse("An error occurred: " + e.getMessage(), null));
        }

    }
    /*@GetMapping("/get-product-image/{prdId}")
    public ResponseEntity<byte[]> getProductImage(@PathVariable Long prdId) {
        try {
            // Get the image byte array from the service
            byte[] imageBytes = productService.getInizialImage(prdId);

            // Set the response content type to image/jpeg (or another type if required)
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG); // Adjust for image type, e.g., PNG

            // Return the image byte array
            return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);

        } catch (ResourceNotFoundException e) {
            // Handle case where product is not found
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            // Handle other errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
*/

    @GetMapping("/all-product")
    public ResponseEntity<ApiResponse> getAllProduct(){

        try {
            List<Product> products =  productService.getAllProducts();
            System.out.println("Products: " + products.size());
            List<ProductDto> convertedProduct = productService.convertToDtoFun(products);
            return ResponseEntity.ok(new ApiResponse("Success",convertedProduct));
        } catch ( Exception e) {
            return ResponseEntity.status(404).body(new ApiResponse(e.getMessage(),null));
        }


    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getProductByID(@PathVariable Long id ){

        try {
            Product product =  productService.getProductById(id);
            ProductDto convertedProduct = productService.convertToDtoMethod(product);
            return ResponseEntity.ok(new ApiResponse("Success",convertedProduct));
        } catch ( ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(new ApiResponse("failed",null));
        }


    }


    @DeleteMapping("delete/{id}")
    public ResponseEntity<ApiResponse> deleteProductByID(@PathVariable Long id ){

        try {

            productService.deleteProductById(id);
            return ResponseEntity.ok(new ApiResponse("product with id "+ id +" deleted successfully ",null));
        } catch ( ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(new ApiResponse("failed",null));
        }
    }





    @GetMapping("/search-brand/{brand}")
    public ResponseEntity<ApiResponse> getProductsByBrand(@PathVariable String brand ){

        try {
            List<Product> products =  productService.getProductsByBrand(brand);
            List<ProductDto> convertedProduct = productService.convertToDtoFun(products);
            return ResponseEntity.ok(new ApiResponse("Success",convertedProduct));
        } catch ( ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(new ApiResponse("failed",null));
        }
    }


    @GetMapping("/search-category-brand")
    public ResponseEntity<ApiResponse> getProductsByCategoryAndBrand(@RequestParam String category ,@RequestParam String brand ){

        try {
            List<Product> products =  productService.getProductsByCategoryAndBrand(category,brand);
            List<ProductDto> convertedProduct = productService.convertToDtoFun(products);
            return ResponseEntity.ok(new ApiResponse("Success",convertedProduct));
        } catch ( ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(new ApiResponse("failed",null));
        }


    }


    @GetMapping("/search-name/{name}")
    public ResponseEntity<ApiResponse> getProductsByName(@PathVariable String name ){

        try {
            List<Product> products =  productService.getProductsByName(name);
            List<ProductDto> convertedProduct = productService.convertToDtoFun(products);
            return ResponseEntity.ok(new ApiResponse("Success",convertedProduct));
        } catch ( ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(new ApiResponse("failed",null));
        }


    }
    @GetMapping("/product-category")
    public ResponseEntity<ApiResponse> getProductsByCategory(@RequestParam String categoryName){
        List<Product> products = productService.getProductsByCategory(categoryName);

       List<ProductDto> productDtoList =  productService.convertToDtoFun(products);

       return ResponseEntity.ok(new ApiResponse("success",productDtoList));
    }



    @PutMapping("/update-product")
    public ResponseEntity<ApiResponse> updateProduct(@RequestBody ProductUpdateRequest productUpdateRequest ){
        try {
            Long product_id = productUpdateRequest.getId();

            Product product =  productService.updateProduct(productUpdateRequest,product_id);
            return ResponseEntity.ok(new ApiResponse("Success", product));
        } catch ( ResourceNotFoundException e) {
            return ResponseEntity.status(500).body(new ApiResponse("failed",null));
        }


    }


}
