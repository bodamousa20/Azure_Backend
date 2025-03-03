package com.wuzzafni.backend.Response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ApiResponse {

    String message ;
    Object data ;


    public ApiResponse(String message, Object data) {
        this.message = message;
        this.data = data;
    }

}
