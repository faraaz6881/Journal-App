package springproject1.journalApp.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import springproject1.journalApp.repository.UserRepository;
import springproject1.journalApp.dto.LoginRequestDto;
import springproject1.journalApp.dto.LoginResponseDto;
import springproject1.journalApp.dto.SignupRequestDto;
import springproject1.journalApp.dto.SignupResponseDto;
import springproject1.journalApp.entity.User;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthUtil authUtil;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;


    public LoginResponseDto login(LoginRequestDto loginRequestDto) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDto.getUsername(), loginRequestDto.getPassword())
        );
        User user = (User) authentication.getPrincipal();

        String token = authUtil.generateAccessToken(user);

        return new LoginResponseDto(token, user.getId());
    }
    public SignupResponseDto signup(SignupRequestDto signupRequestDto){
        User user = userRepository.findByUsername(signupRequestDto.getUsername()).orElse(null);

        if(user!=null)
        {
           throw new IllegalArgumentException("user already exists");
        }
      user = userRepository.save(User.builder()
              .username(signupRequestDto.getUsername())
                      .password(passwordEncoder.encode(signupRequestDto.getPassword()))
                      .build()
              );
        return new SignupResponseDto(user.getId(),user.getUsername());
    }
}
