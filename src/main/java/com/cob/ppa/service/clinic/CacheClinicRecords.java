package com.cob.ppa.service.clinic;


import com.cob.ppa.entity.Clinic;
import com.cob.ppa.repository.ClinicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CacheClinicRecords {
    @Autowired
    ClinicRepository clinicRepository;

    @Cacheable(value = "clinics")
    public List<Clinic> cache(){
        return clinicRepository.findAll().stream()
                .map(clinicRecord -> {
                    Clinic clinic = new Clinic();
                    clinic.setId(clinicRecord.getId());
                    clinic.setName(clinicRecord.getName());
                    return clinic;
                }).collect(Collectors.toList());

    }
}
