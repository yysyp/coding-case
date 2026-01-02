package ps.demo.jpademo.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ps.demo.jpademo.entity.Role;
import ps.demo.jpademo.repository.RoleRepository;

import java.util.Optional;

@Service
public class RoleService {

    @Autowired
    RoleRepository roleRepository;


    @Transactional
    public void testAutoSaveModification() {
        Optional<Role> role = roleRepository.findById(1L);
        role.orElseThrow().setRoleName("haha role name changed"); //This will trigger auto save
    }

    @Transactional
    public void testNotAutoSaveModification(Role role) {
        role.setRoleName("haha not change"); //This will not trigger auto save if the role entity left previous transaction context.
    }

}
