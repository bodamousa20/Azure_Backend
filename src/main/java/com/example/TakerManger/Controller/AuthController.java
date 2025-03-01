package com.example.TakerManger.Controller;

import com.example.TakerManger.Dto.LoginContext;
import com.example.TakerManger.Dto.UserSignUp;
import com.example.TakerManger.Model.Users;
import com.example.TakerManger.Repository.UserRepositiry;
import com.example.TakerManger.Response.ApiResponse;
import com.example.TakerManger.Services.User.UserServices;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
@AllArgsConstructor
public class AuthController {

    private final UserRepositiry userRepositiry;
    private final JwtEncoder jwtEncoder;
    private final UserServices userServices ;

    private final PasswordEncoder passwordEncoder ;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> authLogin(@RequestBody LoginContext logincontext) {
        String currentEmail = logincontext.getEmail();
        String loginPassword = logincontext.getPassword();

        // Fetch user by email
        Users currentUser = userRepositiry.findByEmail(currentEmail);

        if (currentUser == null) {
            // User not found
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse("FAILURE", "Invalid email or password"));
        }

        // Compare passwords (assuming you are using plain text for now)
        if (passwordEncoder.matches(loginPassword, currentUser.getPassword())) {
            // Success - Generate JWT token
            Map<String, Object> response = jwtToken(currentEmail);
            return ResponseEntity.ok(new ApiResponse("SUCCESS", response));
        }

        // Incorrect password
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ApiResponse("FAILURE", "Invalid email or password"));
    }




    public Map<String, Object> jwtToken(String email){
            // Create JWT claims set
            var claims = JwtClaimsSet.builder()
                    .issuer("self")
                    .issuedAt(Instant.now())
                    .expiresAt(Instant.now().plus(1, ChronoUnit.HOURS))  // Token expires in 1 hour
                    .subject(email)
                    .build();

            // Encode the JWT token
            JwtEncoderParameters parameters = JwtEncoderParameters.from(claims);
            String token = jwtEncoder.encode(parameters).getTokenValue();


            // Create response
            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("name",userRepositiry.findByEmail(email).getName());
            response.put("user_id",userRepositiry.findByEmail(email).getId());
            return response ;
        }



    @PostMapping("/signup")
    public ResponseEntity<ApiResponse> createUser(@RequestBody UserSignUp userDto){


        try {
            return ResponseEntity.ok(new ApiResponse("success", userServices.createUser(userDto)));
        } catch (Exception e) {
          return   ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(),null));
        }

    }

}
