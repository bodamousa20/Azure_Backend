package com.example.TakerManger.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor

public class ApiResponse {
    private String message;
    private Object data;

    // Add this constructor explicitly
    public ApiResponse(String message, Object data) {
        this.message = message;
        this.data = data;
    }
}
