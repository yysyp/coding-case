package ps.demo.jpademo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ps.demo.jpademo.entity.Role;

import java.util.List;

// Spring Data JPA creates CRUD implementation at runtime automatically.
public interface RoleRepository extends JpaRepository<Role, Long> {


    @Query(value = """
            WITH RECURSIVE child_roles (id, parent_id, role_name, description, created_time, created_by, level) AS (
            select id, parent_id, role_name, description, created_time, created_by, 1 as level from role_tbl where parent_id = :parentId
            UNION ALL
            select t.id, t.parent_id, t.role_name, t.description, t.created_time, t.created_by, c.level + 1 from role_tbl t
            inner join child_roles c on t.parent_id= c.id
            )
            SELECT distinct * FROM child_roles ;
            """, nativeQuery = true)
    List<Role> findAllChildRoles(@Param("parentId") Long parentId);


}
