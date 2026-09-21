package employee_management.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import employee_management.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByUsername(String username);

  boolean existsByUsername(String username);
}
