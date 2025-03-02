package com.example.TakerManger.Controller;

import com.example.TakerManger.Dto.ImageDto;
import com.example.TakerManger.Model.Image;
import com.example.TakerManger.Repository.ImageRepository;
import com.example.TakerManger.Response.ApiResponse;
import com.example.TakerManger.Services.Image.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.sql.SQLException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static java.lang.System.in;

@RequiredArgsConstructor
@RequestMapping("${api.prefix}/images")
@CrossOrigin(origins = "*")
@RestController
public class ImageController {

    private final ImageService imageService;
    private final ImageRepository imageRepository ;
    @PostMapping("/upload")
    public ResponseEntity<ApiResponse> saveImage(@RequestParam List<MultipartFile>files , @RequestParam Long productId){

        try {

            List<ImageDto> imageDtos = imageService.saveImages(productId,files);
            return ResponseEntity.ok(new ApiResponse("upload succes !",imageDtos));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("upload failed",e.getMessage()));
        }


    }


    @GetMapping("/download/{imageId}")
    public ResponseEntity<ByteArrayResource> downloadImage(@PathVariable Long imageId){
        try {
            Image image = imageService.getImageById(imageId);

            ByteArrayResource resource = new ByteArrayResource(image.getImage().getBytes(1, (int) image.getImage().length()));
            return  ResponseEntity.ok().contentType(MediaType.parseMediaType(image.getFileType()))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" +image.getFileName() + "\"")
                    .body(resource);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }






}
