package com.kamiokaweb.redo.controller.client.io;

import com.kamiokaweb.redo.model.client.Client;

public record ClientResponse(
        Client client
) {
    public static ClientResponse of(Client client) {
        return new ClientResponse(client);
    }
}
