package com.wuzzafni.backend.Controller;

import com.wuzzafni.backend.Dto.SaveJobRequest;
import com.wuzzafni.backend.Dto.saveUserRequest;
import com.wuzzafni.backend.Model.Job;
import com.wuzzafni.backend.Model.UserEntity;
import com.wuzzafni.backend.Response.ApiResponse;
import com.wuzzafni.backend.Services.UserServices;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@RestController
@RequestMapping("${api.prefix}/user")
@AllArgsConstructor
public class UserController {
    private final UserServices userServices ;


    @PostMapping("/create-user")
    public ResponseEntity<ApiResponse>saveUser(@RequestBody saveUserRequest request){
        try {
           UserEntity user = userServices.saveUser(request.getEmail(),request.getName());
           return ResponseEntity.ok(new ApiResponse("success",user ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(),null));

        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse>getUser(@PathVariable Long id ){
        try {
          UserEntity user =   userServices.getUser(id);
          userServices.convertUserToDto(user);
            return ResponseEntity.ok(new ApiResponse("success", userServices.convertUserToDto(user)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(),null));

        }
    }
    @PostMapping("/saveJob")
    public ResponseEntity<ApiResponse>saveJobByUser(@ModelAttribute SaveJobRequest request){
        try {
           Job job = userServices.saveJobByUser(request);
            return ResponseEntity.ok(new ApiResponse("success",userServices.convertToDtoMethod(job) ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(),null));

        }
    }


    @GetMapping("/job/{jobId}")
    public ResponseEntity<ApiResponse>saveJobByUser(@PathVariable Long jobId){
        try {
            return ResponseEntity.ok(new ApiResponse("success",userServices.getJob(jobId)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("failed",null));

        }
    }

    @GetMapping("/jobs/{userId}")
    public ResponseEntity<ApiResponse>getUserJobs(@PathVariable Long userId){
        try {
            return ResponseEntity.ok(new ApiResponse("success",userServices.getUserJobs(userId)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("failed",null));

        }
    }

    @DeleteMapping("{userId}/delete-job/{jobId}")
    public ResponseEntity<ApiResponse>deleteJob(@PathVariable Long userId,@PathVariable Long jobId ){
        try {
            userServices.deleteUserJobs(userId,jobId);
            return ResponseEntity.ok(new ApiResponse("deleted successfully",null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("failed",null));

        }
    }




    @Autowired
    private JavaMailSender mailSender;

    public String sendHtmlEmail(String receiver, String link,String name) throws MessagingException, IOException {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom("reikomahmoud208@gmail.com");
            helper.setTo(receiver);
            helper.setSubject("Welcome to Wuzzufni");

            // Read the HTML template
            String emailContent;
            try (var inputStream = Objects.requireNonNull(UserController.class.getResourceAsStream("/templates/Wuzzufni_Email.html"))) {
                emailContent = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            }

            // Replace the placeholder with the actual link
            emailContent = emailContent.replace("{{link}}", link);
            emailContent = emailContent.replace("{{name}}", name);
            helper.addInline("wuzzufni", new ClassPathResource("static/wuzzufni.png"));


            // Set the email content
            helper.setText(emailContent, true);

            // Add inline image

            mailSender.send(message);
            return "email sent";
        } catch (MessagingException | IOException | MailException e) {
            throw new RuntimeException(e);
        }
    }





}
