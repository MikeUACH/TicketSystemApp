package org.sistemas.ticketsystemapp.repository;


import java.util.Optional;
import org.sistemas.ticketsystemapp.entity.AdminUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminUserRepository extends JpaRepository<AdminUser, Long> {

    Optional<AdminUser> findByUsername(String username);
}
