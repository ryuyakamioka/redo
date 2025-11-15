package com.kamiokaweb.redo.controller.company;

import com.kamiokaweb.redo.controller.company.io.CompanyListResponse;
import com.kamiokaweb.redo.usecase.CompanyUseCase;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = {"http://localhost:3000", "http://15.168.89.85"})
public class CompanyController {
    private final CompanyUseCase companyUseCase;

    public CompanyController(CompanyUseCase companyUseCase) {
        this.companyUseCase = companyUseCase;
    }

    @GetMapping("/companies")
    public CompanyListResponse getCompanies() {
        return CompanyListResponse.of(companyUseCase.getList());
    }
}
