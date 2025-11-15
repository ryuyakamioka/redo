package com.kamiokaweb.redo.controller.company.io;

import com.kamiokaweb.redo.model.company.Company;

public record CompanyResponse(
        Company company
) {
    public static CompanyResponse of(Company company) {
        return new CompanyResponse(company);
    }
}
