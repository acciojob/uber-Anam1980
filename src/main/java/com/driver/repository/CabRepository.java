package com.driver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.driver.model.Cab;

@Repository
public interface CabRepository extends JpaRepository<Cab, Integer> {

    @Query(value = "select c from Cab c where c.perKmRate=:10 and c.available=:true", nativeQuery = true)
    Cab getDefaultCab();
}
