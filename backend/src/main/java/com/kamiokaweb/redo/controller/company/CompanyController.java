package com.kamiokaweb.redo.controller.company;

import com.kamiokaweb.redo.controller.company.io.CompanyListResponse;
import com.kamiokaweb.redo.controller.company.io.CompanyRequest;
import com.kamiokaweb.redo.controller.company.io.CompanyResponse;
import com.kamiokaweb.redo.model.company.Company;
import com.kamiokaweb.redo.usecase.CompanyUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class CompanyController {
    private final CompanyUseCase companyUseCase;

    public CompanyController(CompanyUseCase companyUseCase) {
        this.companyUseCase = companyUseCase;
    }

    @GetMapping("/companies")
    public CompanyListResponse getCompanies() {
        return CompanyListResponse.of(companyUseCase.getList());
    }

    @PostMapping("/company")
    public ResponseEntity<CompanyResponse> createCompany(@RequestBody CompanyRequest request) {
        Company created = companyUseCase.create(request.company());
        return ResponseEntity.ok(CompanyResponse.of(created));
    }

    @PutMapping("/company/{id}")
    public ResponseEntity<CompanyResponse> updateCompany(@PathVariable Long id, @RequestBody CompanyRequest request) {
        Company updated = companyUseCase.update(request.company());
        return ResponseEntity.ok(CompanyResponse.of(updated));
    }

    @DeleteMapping("/company/{id}")
    public ResponseEntity<Void> deleteCompany(@PathVariable Long id) {
        companyUseCase.delete(id);
        return ResponseEntity.ok().build();
    }
}
