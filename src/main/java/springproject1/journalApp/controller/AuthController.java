package springproject1.journalApp.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springproject1.journalApp.dto.LoginRequestDto;
import springproject1.journalApp.dto.LoginResponseDto;
import springproject1.journalApp.dto.SignupRequestDto;
import springproject1.journalApp.dto.SignupResponseDto;
import springproject1.journalApp.security.AuthService;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> Login(@RequestBody LoginRequestDto loginRequestDto){
         System.out.println(loginRequestDto);
         return ResponseEntity.ok(authService.login(loginRequestDto));
    }

    @PostMapping("/signup")
    public ResponseEntity<SignupResponseDto> signup(@RequestBody SignupRequestDto signupRequestDto){
        return ResponseEntity.ok(authService.signup(signupRequestDto));
    }
}
