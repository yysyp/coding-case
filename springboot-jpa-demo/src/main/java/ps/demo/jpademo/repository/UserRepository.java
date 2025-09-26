package ps.demo.jpademo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ps.demo.jpademo.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}