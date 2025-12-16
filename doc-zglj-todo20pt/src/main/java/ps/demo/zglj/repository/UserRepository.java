package ps.demo.zglj.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ps.demo.zglj.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}