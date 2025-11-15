package com.kamiokaweb.redo.repository.client;

import com.kamiokaweb.redo.model.client.Client;
import com.kamiokaweb.redo.model.client.ClientId;

import java.util.List;
import java.util.Optional;

public interface ClientRepository {
    Optional<Client> get(ClientId clientId);
    List<Client> getList();
    Client save(Client client);
    void delete(ClientId clientId);
}
