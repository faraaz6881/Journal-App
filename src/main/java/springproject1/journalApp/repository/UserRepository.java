package springproject1.journalApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import springproject1.journalApp.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
    void deleteByUsername(String username);

}
//controller ---->> service -->  repository