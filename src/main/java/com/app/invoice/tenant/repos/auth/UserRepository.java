package com.app.invoice.tenant.repos.auth;



import com.app.invoice.tenant.entity.auth.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT DISTINCT u.passcode FROM User u")
    List<String> findDistinctPasscodes();

    Optional<User> findByPhoneNo(String phoneNo);

    Optional<User> findByEmail(String email);

    Optional<User> findByPasscode(String passcode);

    Optional<User> findByResetToken(String resetToken);

    Optional<User> findByNurseId(Long nurseId);



}
