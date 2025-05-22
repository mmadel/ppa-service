package com.cob.ppa.repository;

import com.cob.ppa.entity.Clinic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ClinicRepository extends JpaRepository<Clinic,Long> {
    Optional<Clinic> findByName(String name);
}
