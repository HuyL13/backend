package com.bluemoonproject.repository;

import com.bluemoonproject.entity.User;
import com.bluemoonproject.enums.ResidencyStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    Optional<User> findByUsername(String username);
    //
    Optional<User> findByEmail(String email);

    // Có cần thêm tìm findByResidencyStatus không ?
    List<User> findByResidencyStatus(ResidencyStatus residencyStatus);

    List<User> findByRoles_Name(String adminRoles);

    @Query("SELECT u FROM User u WHERE " +
            "(LOWER(u.username) LIKE LOWER(CONCAT('%', :username, '%')) OR :username IS NULL) AND " +
            "(LOWER(u.firstName) LIKE LOWER(CONCAT('%', :firstName, '%')) OR :firstName IS NULL) AND " +
            "(LOWER(u.lastName) LIKE LOWER(CONCAT('%', :lastName, '%')) OR :lastName IS NULL) AND " +
            "(LOWER(u.email) LIKE LOWER(CONCAT('%', :email, '%')) OR :email IS NULL)")
    Page<User> findUsersBySearchParams(String username, String firstName, String lastName, String email, Pageable pageable);
}