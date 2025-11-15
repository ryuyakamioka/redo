package com.kamiokaweb.redo.repository.company;

import com.kamiokaweb.redo.model.company.Company;
import com.kamiokaweb.redo.model.company.CompanyId;

import java.util.List;
import java.util.Optional;

public interface CompanyRepository {
    Optional<Company> get(CompanyId companyId);
    List<Company> getList();
}
