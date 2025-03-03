package com.wuzzafni.backend.Services;

import com.wuzzafni.backend.Dto.JobDto;
import com.wuzzafni.backend.Dto.SaveJobRequest;
import com.wuzzafni.backend.Dto.userDto;
import com.wuzzafni.backend.Model.Job;
import com.wuzzafni.backend.Model.UserEntity;
import jakarta.mail.MessagingException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public interface IUserServices {

    public userDto convertUserToDto(UserEntity user);
    UserEntity saveUser(String email ,String name ) throws MessagingException, IOException;


    UserEntity getUser(Long id );

    Job saveJobByUser(SaveJobRequest request) ;


    Job getSavedJob(Long userId);

     JobDto convertToDtoMethod(Job job);

     JobDto getJob(Long JobId);
    List <JobDto> getUserJobs(Long userId);

    void deleteUserJobs(Long userId,Long jobId);

}
