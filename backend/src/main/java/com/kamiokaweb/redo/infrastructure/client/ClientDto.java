package com.kamiokaweb.redo.infrastructure.client;

import com.kamiokaweb.redo.model.client.Client;
import com.kamiokaweb.redo.model.client.ClientAbbreviation;
import com.kamiokaweb.redo.model.client.ClientId;
import com.kamiokaweb.redo.model.client.ClientName;
import com.kamiokaweb.redo.model.company.Company;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table("clients")
public record ClientDto(
        @Id
        Long id,
        String name,
        String abbreviation,
        Long companyId,
        Boolean showClientNameInDescription,
        LocalDateTime createdAt
) {
    public Client from(Company company) {
        return new Client(
                new ClientId(id),
                new ClientName(name),
                new ClientAbbreviation(abbreviation),
                company,
                showClientNameInDescription != null ? showClientNameInDescription : true
        );
    }
}
