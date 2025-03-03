package com.wuzzafni.backend.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaveJobRequest {
    private Long id ;
    private String title ;
    private String description ;
    private String jobLink ;
    private String ImageLink ;


}
