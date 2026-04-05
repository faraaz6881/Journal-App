package springproject1.journalApp.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import springproject1.journalApp.repository.UserRepository;
import springproject1.journalApp.entity.User;

import java.util.List;
import java.util.Optional;


@Component
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void saveEntry(User user){
        user.setPassword(user.getPassword());

        userRepository.save(user);
    }
    public User findByUsername(String username){
        return userRepository.findByUsername(username)
                .orElse(null);
    }

    public List<User> getAll(){
        return userRepository.findAll();
    }
    public Optional<User> findById(Long id)
    {
        return userRepository.findById(id);
    }
    public void DeleteById(Long id){
        userRepository.deleteById(id);
    }

    public  void DeleteByUserName(String username){userRepository.deleteByUsername(username);
    }
}


//controller ---> service ---> repository