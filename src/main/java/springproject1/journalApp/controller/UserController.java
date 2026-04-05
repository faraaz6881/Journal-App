package springproject1.journalApp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import springproject1.journalApp.entity.User;
import springproject1.journalApp.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {


    private final UserService userService;




        @GetMapping
        public List<User> getAllUsers(){
            return userService.getAll();
        }


    @PutMapping("/{userName}")
    public ResponseEntity<?> updateUser(@RequestBody User user,@PathVariable String userName)
        {
           User userInDb =  userService.findByUsername(userName);
           if(userInDb !=null)
           {
               userInDb.setUsername(user.getUsername());

               userService.saveEntry(userInDb);
           }
           return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }




}


