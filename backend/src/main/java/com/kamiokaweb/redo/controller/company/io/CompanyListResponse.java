package com.kamiokaweb.redo.controller.company.io;

import com.kamiokaweb.redo.model.company.Company;

import java.util.List;

public record CompanyListResponse(
        List<Company> companyList
) {
    public static CompanyListResponse of(List<Company> companies) {
        return new CompanyListResponse(companies);
    }
}
