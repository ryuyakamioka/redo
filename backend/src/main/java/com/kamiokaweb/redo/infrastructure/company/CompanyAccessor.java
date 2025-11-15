package com.kamiokaweb.redo.infrastructure.company;

import org.springframework.data.repository.CrudRepository;

public interface CompanyAccessor extends CrudRepository<CompanyDto, Long> {
}
