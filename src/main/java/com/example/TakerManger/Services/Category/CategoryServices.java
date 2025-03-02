package com.example.TakerManger.Services.Category;

import com.example.TakerManger.Dto.ProductDto;
import com.example.TakerManger.Dto.categoryDto;
import com.example.TakerManger.Exception.ResourceNotFoundException;
import com.example.TakerManger.Model.Category;
import com.example.TakerManger.Repository.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Blob;
import java.util.List;
import java.util.Optional;
@Service
public class CategoryServices implements ICategoryService {
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper ;

    public CategoryServices(CategoryRepository categoryRepository, ModelMapper modelMapper) {
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Category not found!"));
    }

    @Override
    public Category getCategoryByName(String name) {
        return categoryRepository.findByName(name);
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public List<categoryDto> convertCategoryToDtoFun(List<Category> categories) {
        return categories.stream().map(category -> convertCategoryToDtoMethod(category)).toList();
    }

    @Override
    public categoryDto convertCategoryToDtoMethod(Category category) {
        return  modelMapper.map(category,categoryDto.class);
    }


    @Override
     public Category addCategory(String name, MultipartFile file) throws IOException {
               Optional<Category> category = Optional.ofNullable(categoryRepository.findByName(name));

            if (category.isEmpty()){
                Category cat = new Category();
                cat.setName(name);
                cat.setImage(file.getBytes());
               return categoryRepository.save(cat);

            }
            else{
                throw new ResourceNotFoundException("this category is already exist");
            }
     }

   @Override
    public Category updateCategory(Category category, Long id) {
        return Optional.ofNullable(getCategoryById(id)).map(oldCategory -> {
            oldCategory.setName(category.getName());
            return categoryRepository.save(oldCategory);
        }) .orElseThrow(()-> new ResourceNotFoundException("Category not found!"));
    }


    @Override
    public void deleteCategoryById(Long id) {
        categoryRepository.findById(id)
                .ifPresentOrElse(categoryRepository::delete, () -> {
                    throw new ResourceNotFoundException("Category not found!");
                });

    }


}
