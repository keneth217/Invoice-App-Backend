package com.app.invoice.master.repos;

import com.app.invoice.master.entity.Business;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BusinessRepository extends JpaRepository<Business, UUID> {
   Optional<Business> findById(UUID id);

    Optional <Business> findByNameIgnoreCase(String name);



    Business findByEmail(String email);

    Business findByPhone(String phone);


    List<Business> findByActivatedTrue();

    List<Business> findByActivatedFalse();

    Long countByActivatedTrue();

    Long countByActivatedFalse();

 @Query("SELECT b.businessCode FROM Business b WHERE b.businessCode IS NOT NULL ORDER BY b.businessCode DESC LIMIT 1")
 String findLastBusinessCode();

    List<Business> findAllByDeletedFalse();

 Optional<Business> findByDeletedFalse();

 Optional<Business> findByBusinessCode(String businessCode);

 boolean existsByBusinessCode(String code);

 boolean existsByName(String name);
}
