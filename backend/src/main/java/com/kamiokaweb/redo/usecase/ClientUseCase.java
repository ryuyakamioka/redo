package com.kamiokaweb.redo.usecase;

import com.kamiokaweb.redo.model.client.Client;
import com.kamiokaweb.redo.repository.client.ClientRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientUseCase {
    private final ClientRepository clientRepository;

    public ClientUseCase(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public List<Client> getList() {
        return clientRepository.getList();
    }

    public Client create(Client client) {
        return clientRepository.save(client);
    }

    public Client update(Client client) {
        return clientRepository.save(client);
    }

    public void delete(Long clientId) {
        clientRepository.delete(new com.kamiokaweb.redo.model.client.ClientId(clientId));
    }
}
