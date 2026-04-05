package springproject1.journalApp.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import springproject1.journalApp.repository.UserRepository;
import springproject1.journalApp.entity.User;

import java.io.IOException;


@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilterInternal extends OncePerRequestFilter {

    private final UserRepository userRepository;
    private final AuthUtil authUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
       log.info("incoming request : {}",request.getRequestURI());

       final String requestTokenHeader = request.getHeader("Authorization");
       if(requestTokenHeader == null || !requestTokenHeader.startsWith("Bearer ")) {
           filterChain.doFilter(request,response);
           return;
       }
       String token = requestTokenHeader.split("Bearer ")[1];
       String username = authUtil.getUserNameFromToken(token);

       if(username !=null && SecurityContextHolder.getContext().getAuthentication()==null){
           User user =userRepository.findByUsername(username).orElseThrow();
           UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                   new UsernamePasswordAuthenticationToken(user , null, user.getAuthorities());
             SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

       }
       filterChain.doFilter(request,response);

    }
}
