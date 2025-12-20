package com.kamiokaweb.redo.infrastructure.company;

import com.kamiokaweb.redo.model.company.Company;
import com.kamiokaweb.redo.model.company.CompanyId;
import com.kamiokaweb.redo.model.company.CompanyName;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table("companies")
public record CompanyDto(
        @Id
        Long id,
        String name,
        Boolean withholdingTax,
        Long freeePartnerId,
        LocalDateTime createdAt
) {
    public Company from() {
        return new Company(
                new CompanyId(id),
                new CompanyName(name),
                withholdingTax,
                freeePartnerId
        );
    }
}
