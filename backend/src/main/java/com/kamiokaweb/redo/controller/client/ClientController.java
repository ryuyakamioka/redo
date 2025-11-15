package com.kamiokaweb.redo.controller.client;

import com.kamiokaweb.redo.controller.client.io.ClientListResponse;
import com.kamiokaweb.redo.usecase.ClientUseCase;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = {"http://localhost:3000", "http://15.168.89.85"})
public class ClientController {
    private final ClientUseCase clientUseCase;

    public ClientController(ClientUseCase clientUseCase) {
        this.clientUseCase = clientUseCase;
    }

    @GetMapping("/clients")
    public ClientListResponse getClients() {
        return ClientListResponse.of(clientUseCase.getList());
    }
}
