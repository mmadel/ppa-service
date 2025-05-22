package com.cob.ppa.repository;


import com.cob.ppa.entity.ExcelTemplate;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ExcelTemplateRepository extends CrudRepository<ExcelTemplate, Long> {

    Optional<ExcelTemplate> findByTemplateName(String name);
}
