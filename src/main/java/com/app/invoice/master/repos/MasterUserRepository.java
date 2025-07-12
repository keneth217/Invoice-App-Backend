package com.app.invoice.master.repos;

import com.app.invoice.master.entity.MasterUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MasterUserRepository extends JpaRepository<MasterUser, Long> {
    @Query("SELECT DISTINCT u.passcode FROM MasterUser u")
    List<String> findDistinctPasscodes();

    Optional<MasterUser> findByPhoneNo(String phoneNo);

    Optional<MasterUser> findByEmail(String email);

    Optional<MasterUser> findByPasscode(String passcode);

    Optional<MasterUser> findByResetToken(String resetToken);
}
