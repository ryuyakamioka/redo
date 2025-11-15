package com.kamiokaweb.redo.repository.client;

import com.kamiokaweb.redo.infrastructure.client.ClientAccessor;
import com.kamiokaweb.redo.infrastructure.client.ClientDto;
import com.kamiokaweb.redo.infrastructure.company.CompanyAccessor;
import com.kamiokaweb.redo.model.client.Client;
import com.kamiokaweb.redo.model.client.ClientId;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Repository
public class ClientRepositoryImpl implements ClientRepository {
    private final ClientAccessor clientAccessor;
    private final CompanyAccessor companyAccessor;

    public ClientRepositoryImpl(ClientAccessor clientAccessor, CompanyAccessor companyAccessor) {
        this.clientAccessor = clientAccessor;
        this.companyAccessor = companyAccessor;
    }

    @Override
    public Optional<Client> get(ClientId clientId) {
        return clientAccessor.findById(clientId.value())
                .flatMap(clientDto -> companyAccessor.findById(clientDto.companyId())
                        .map(companyDto -> clientDto.from(companyDto.from())));
    }

    @Override
    public List<Client> getList() {
        return StreamSupport.stream(clientAccessor.findAll().spliterator(), false)
                .map(clientDto -> companyAccessor.findById(clientDto.companyId())
                        .map(companyDto -> clientDto.from(companyDto.from()))
                        .orElseThrow())
                .toList();
    }

    @Override
    public Client save(Client client) {
        ClientDto dto = new ClientDto(
                client.clientId() != null ? client.clientId().value() : null,
                client.clientName().value(),
                client.clientAbbreviation().value(),
                client.company().companyId().value(),
                null
        );
        ClientDto saved = clientAccessor.save(dto);
        return companyAccessor.findById(saved.companyId())
                .map(companyDto -> saved.from(companyDto.from()))
                .orElseThrow();
    }

    @Override
    public void delete(ClientId clientId) {
        clientAccessor.deleteById(clientId.value());
    }
}
