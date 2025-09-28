package cz.ivosahlik.ecommerce.repositories;

import cz.ivosahlik.ecommerce.model.AppRole;
import cz.ivosahlik.ecommerce.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleName(AppRole appRole);
}
