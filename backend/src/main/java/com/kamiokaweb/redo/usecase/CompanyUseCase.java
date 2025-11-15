package com.kamiokaweb.redo.usecase;

import com.kamiokaweb.redo.model.company.Company;
import com.kamiokaweb.redo.repository.company.CompanyRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyUseCase {
    private final CompanyRepository companyRepository;

    public CompanyUseCase(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public List<Company> getList() {
        return companyRepository.getList();
    }

    public Company create(Company company) {
        return companyRepository.save(company);
    }

    public Company update(Company company) {
        return companyRepository.save(company);
    }

    public void delete(Long companyId) {
        companyRepository.delete(new com.kamiokaweb.redo.model.company.CompanyId(companyId));
    }
}
