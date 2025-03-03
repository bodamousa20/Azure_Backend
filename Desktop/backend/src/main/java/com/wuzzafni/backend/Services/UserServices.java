package com.wuzzafni.backend.Services;

import com.wuzzafni.backend.Controller.UserController;
import com.wuzzafni.backend.Dto.JobDto;
import com.wuzzafni.backend.Dto.SaveJobRequest;
import com.wuzzafni.backend.Dto.userDto;
import com.wuzzafni.backend.Exception.ResourceNotFoundException;
import com.wuzzafni.backend.Model.Job;
import com.wuzzafni.backend.Model.UserEntity;
import com.wuzzafni.backend.Repositary.ImageRepository;
import com.wuzzafni.backend.Repositary.JobRepository;
import com.wuzzafni.backend.Repositary.UserRepository;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@AllArgsConstructor
public class UserServices implements IUserServices {


    private final UserRepository userRepository;

    private final JobRepository jobRepository;
    private final ImageRepository imageRepository;

    private final ModelMapper modelMapper;
    private final UserController userController;


    @Override
    public UserEntity saveUser(String email, String name) throws MessagingException, IOException {
        UserEntity user = userRepository.findByUserEmail(email);
        if (user == null) {
            String baseUrl ="https://graduatebackend-hgabf0a7dngjfuh8.uaenorth-01.azurewebsites.net/api/v1/user/";
            UserEntity newUser = new UserEntity();
            newUser.setUserEmail(email);
            newUser.setName(name);
            //send email if the user is not exist
            userController.sendHtmlEmail(user.getUserEmail(),  baseUrl + user.getId(), user.getName());
            return userRepository.save(newUser);

        } else {
            return user;

        }
    }

    @Override
    public UserEntity getUser(Long id) {
        return userRepository.findById(id).
                orElseThrow(() -> new ResourceNotFoundException("the user with id is not found "));
    }

    @Override
    public Job saveJobByUser(SaveJobRequest request) {
        UserEntity user = userRepository.findById(request.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + request.getId()));

        try {
            Job newJob = new Job();
            newJob.setJobLink(request.getJobLink());
            newJob.setTitle(request.getTitle());
            newJob.setImageLink(request.getImageLink());
            newJob.setDescription(request.getDescription());



            Job savedJob = jobRepository.save(newJob);
            user.getSavedJobs().add(savedJob);
            userRepository.save(user);
            return savedJob;
        } catch (Exception e) {
            throw new RuntimeException("Error saving job: " + e.getMessage());
        }
    }

    @Override
    public Job getSavedJob(Long userId) {
        return null;
    }


    @Override
    public JobDto convertToDtoMethod(Job job) {
        JobDto jobDto = modelMapper.map(job, JobDto.class);
        jobDto.setImageLink(job.getImageLink()); // Explicitly setting it
        return jobDto;
    }

    @Override
    public userDto convertUserToDto(UserEntity user) {
   List<Job> jobs =   user.getSavedJobs();
   List<JobDto> jobDtos =jobs.stream().map(this::convertToDtoMethod).toList();
        userDto userDto = modelMapper.map(user, userDto.class);
        userDto.setSavedJobs(jobDtos);

        return userDto;
    }

    @Override
    public JobDto getJob(Long jobId) {
      Job job =  jobRepository.findById(jobId).orElseThrow(()->new ResourceNotFoundException("The user is not found"));
            return convertToDtoMethod(job);
    }

    @Override
    public List<JobDto> getUserJobs(Long userId) {
        UserEntity user =  userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("The user is not found"));
        List<Job> jobs = user.getSavedJobs();

        return jobs.stream().map(this::convertToDtoMethod).toList();
    }

    @Override
    public void deleteUserJobs(Long userId, Long jobId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("The user is not found"));

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("The job is not found"));

        if (user.getSavedJobs().contains(job)) {
            user.getSavedJobs().remove(job); // Remove the job from the user's saved jobs
            userRepository.save(user); // Save the updated user entity
        } else {
            throw new ResourceNotFoundException("The job is not found in the user's saved jobs");
        }
    }





}

