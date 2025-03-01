package com.example.TakerManger.Controller;

import com.example.TakerManger.Exception.ResourceNotFoundException;
import com.example.TakerManger.Model.Category;
import com.example.TakerManger.Services.Category.CategoryServices;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.TakerManger.Response.ApiResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("${api.prefix}/category")
@CrossOrigin(origins = "*")
public class CategoryController {

    private final CategoryServices categoryService;

    public CategoryController(CategoryServices categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllCategory() {
        try {
            List<Category> categories = categoryService.getAllCategories();

            return ResponseEntity.ok(new ApiResponse("Success", categoryService.convertCategoryToDtoFun(categories)));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(500).body(new ApiResponse("Failed", null));
        }
    }


    @PostMapping("/create-category")
    public ResponseEntity<ApiResponse> createCategory(@RequestParam("name") String name,
                                                      @RequestParam("image") MultipartFile image) {
        try {
            Category categories = categoryService.addCategory(name,image);
            return ResponseEntity.ok(new ApiResponse("Success", categories));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
