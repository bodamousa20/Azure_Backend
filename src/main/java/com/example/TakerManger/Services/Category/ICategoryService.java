package com.example.TakerManger.Services.Category;


import com.example.TakerManger.Dto.ImageDto;
import com.example.TakerManger.Dto.ProductDto;
import com.example.TakerManger.Dto.categoryDto;
import com.example.TakerManger.Model.Category;
import com.example.TakerManger.Model.Image;
import com.example.TakerManger.Model.Product;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ICategoryService {
    Category getCategoryById(Long id);

    Category getCategoryByName(String name);

    List<Category> getAllCategories();


//    Category updateCategory(Category category, Long id);



    public List<categoryDto> convertCategoryToDtoFun(List<Category> categories);


    public categoryDto convertCategoryToDtoMethod(Category category);

    Category addCategory(String name, MultipartFile file) throws IOException;

    Category updateCategory(Category category, Long id);

    void deleteCategoryById(Long id);
}
