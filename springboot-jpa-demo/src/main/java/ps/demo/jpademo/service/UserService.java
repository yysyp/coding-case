package ps.demo.jpademo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ps.demo.jpademo.entity.User;
import ps.demo.jpademo.repository.UserRepository;

import java.util.List;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Value("${security.default-user}")
    private String defaultUserString;
    @Value("${security.default-password}")
    private String defaultPasswordString;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            if (username.equals(defaultUserString)) {

                User defaultUser = new User();
                defaultUser.setUsername(defaultUserString);
                defaultUser.setPassword(new BCryptPasswordEncoder().encode(defaultPasswordString));
                defaultUser.setRoles("ROLE_ADMIN");
                return defaultUser;
            }
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return user;
    }
}