package com.kamiokaweb.redo.controller.client.io;

import com.kamiokaweb.redo.model.client.Client;

import java.util.List;

public record ClientListResponse(
        List<Client> clientList
) {
    public static ClientListResponse of(List<Client> clients) {
        return new ClientListResponse(clients);
    }
}
