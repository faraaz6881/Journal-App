package springproject1.journalApp.controller;


import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import springproject1.journalApp.dto.LoginRequestDto;
import springproject1.journalApp.dto.LoginResponseDto;
import springproject1.journalApp.dto.SignupRequestDto;
import springproject1.journalApp.dto.SignupResponseDto;
import springproject1.journalApp.security.AuthService;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(
            @RequestBody LoginRequestDto loginRequestDto,
            HttpServletResponse response) {

        LoginResponseDto loginResponse = authService.login(loginRequestDto);

        // Write JWT as HttpOnly cookie
        Cookie jwtCookie = new Cookie("jwt", loginResponse.getJwt());
        jwtCookie.setHttpOnly(true);   // so that JS cannot access this cookie
        jwtCookie.setSecure(false);    // to be set true in production (HTTPS only)
        jwtCookie.setPath("/");        // to make sure that cookie is valid for all endpoints
        jwtCookie.setMaxAge(60 * 60* 10);  // setting the age of cookies

        response.addCookie(jwtCookie);

        return ResponseEntity.ok(new LoginResponseDto(null, loginResponse.getUserid()));
    }

    @PostMapping("/signup")
    public ResponseEntity<SignupResponseDto> signup(@RequestBody SignupRequestDto signupRequestDto){
        return ResponseEntity.ok(authService.signup(signupRequestDto));
    }
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletResponse response) {
        Cookie jwtCookie = new Cookie("jwt", null);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setSecure(false);
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(0);  // instantly expires the cookie
        response.addCookie(jwtCookie);
        return ResponseEntity.ok("Logged out successfully.");
    }
}
