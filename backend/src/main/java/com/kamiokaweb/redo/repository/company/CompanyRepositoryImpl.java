package com.kamiokaweb.redo.repository.company;

import com.kamiokaweb.redo.infrastructure.company.CompanyAccessor;
import com.kamiokaweb.redo.infrastructure.company.CompanyDto;
import com.kamiokaweb.redo.model.company.Company;
import com.kamiokaweb.redo.model.company.CompanyId;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Repository
public class CompanyRepositoryImpl implements CompanyRepository {
    private final CompanyAccessor companyAccessor;

    public CompanyRepositoryImpl(CompanyAccessor companyAccessor) {
        this.companyAccessor = companyAccessor;
    }

    @Override
    public Optional<Company> get(CompanyId companyId) {
        return companyAccessor.findById(companyId.value())
                .map(CompanyDto::from);
    }

    @Override
    public List<Company> getList() {
        return StreamSupport.stream(companyAccessor.findAll().spliterator(), false)
                .map(CompanyDto::from)
                .toList();
    }
}
