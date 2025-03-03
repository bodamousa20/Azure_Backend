package com.wuzzafni.backend.Dto;

import com.wuzzafni.backend.Model.Job;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
public class userDto {
    private Long id ;

    private String name ;
    private String userEmail ;

    private List<JobDto> savedJobs ;

}
