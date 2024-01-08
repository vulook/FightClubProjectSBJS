package edu.cbsystematics.com.fightclubprojectsbjs.repository;

import edu.cbsystematics.com.fightclubprojectsbjs.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.LocalDate;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE LOWER(u.username) = LOWER(:username)")
    User findByUsername(@Param("username") String username);

    @Query("SELECT u FROM User u WHERE LOWER(u.email) = LOWER(:email)")
    User findByEmail(@Param("email") String email);

    @Query("SELECT u FROM User u WHERE u.verificationCode = :verificationCode")
    User findByVerificationCode(@Param("verificationCode") String verificationCode);

}
