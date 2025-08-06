package com.cityzen.auth.repository;
import com.cityzen.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByAadhaar(String aadhaar);
    long countByRolesContaining(com.cityzen.auth.enums.Role role);
    Optional<User> findByRefreshToken(String refreshToken);
    long countByGenderIgnoreCase(String gender);

    Optional<User> findUserByAadhaar(String email);
}