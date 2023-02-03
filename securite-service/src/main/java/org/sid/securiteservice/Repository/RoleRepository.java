package org.sid.securiteservice.Repository;

import org.sid.securiteservice.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {
       Role findByName(String name);
}
