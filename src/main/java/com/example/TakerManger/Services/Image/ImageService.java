package com.example.TakerManger.Services.Image;

import com.example.TakerManger.Dto.ImageDto;
import com.example.TakerManger.Exception.ResourceNotFoundException;
import com.example.TakerManger.Model.Image;
import com.example.TakerManger.Model.Product;
import com.example.TakerManger.Repository.ImageRepository;
import com.example.TakerManger.Response.ApiResponse;
import com.example.TakerManger.Services.Product.IProductService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageService implements IImageService {
    private final ImageRepository imageRepository;
    private final IProductService productService;


    @Override
    public Image getImageById(Long id) {
        return imageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No image found with id: " + id));
    }

    @Override
    public void deleteImageById(Long id) {
        imageRepository.findById(id).ifPresentOrElse(imageRepository::delete, () -> {
            throw new ResourceNotFoundException("No image found with id: " + id);
        });

    }


    @Override
    public List<ImageDto> saveImages(Long productId, List<MultipartFile> files) {
        Product currentProduct = productService.getProductById(productId);
        List<ImageDto> savedImageDtos = new ArrayList<>();

        for (MultipartFile file : files) {
            try {
                // Create and populate Image entity
                Image image = new Image();
                image.setFileName(file.getOriginalFilename());
                image.setFileType(file.getContentType());
                image.setImage(new SerialBlob(file.getBytes()));
                image.setProduct(currentProduct);
            
                // Save the image to generate an ID
                String domain = "https://ecommeracebackabdo-hrhbbubqaba9e7et.uaenorth-01.azurewebsites.net/" ;
                Image savedImage = imageRepository.save(image);

                // Set the correct download URL using the generated ID
                String downloadUrl = domain + savedImage.getId();
                savedImage.setDownloadUrl(downloadUrl);
                savedImage = imageRepository.save(savedImage);

                // Create ImageDto and add to the list
                ImageDto imageDto = new ImageDto();
                imageDto.setId(savedImage.getId());
                imageDto.setFileName(savedImage.getFileName());
                imageDto.setDownloadUrl(savedImage.getDownloadUrl());
                savedImageDtos.add(imageDto);

            } catch (IOException | SQLException e) {
                throw new RuntimeException("Error saving image: " + e.getMessage());
            }
        }

        return savedImageDtos;
    }





//update image using the past image id
    @Override
    public void updateImage(MultipartFile file, Long imageId) {
        Image image = getImageById(imageId); //find image
        try { // apply the change
            image.setFileName(file.getOriginalFilename());
            image.setFileType(file.getContentType());
            /*Converts the uploaded file
             into a byte array
             and wraps it in a SerialBlob object.
             SerialBlob is used to store binary large objects (BLOBs) in databases
            * */

            image.setImage(new SerialBlob(file.getBytes()));

            imageRepository.save(image);
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e.getMessage());
        }

    }
}
