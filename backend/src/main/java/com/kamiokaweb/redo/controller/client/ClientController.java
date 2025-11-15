package com.kamiokaweb.redo.controller.client;

import com.kamiokaweb.redo.controller.client.io.ClientListResponse;
import com.kamiokaweb.redo.controller.client.io.ClientRequest;
import com.kamiokaweb.redo.controller.client.io.ClientResponse;
import com.kamiokaweb.redo.model.client.Client;
import com.kamiokaweb.redo.usecase.ClientUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/client")
    public ResponseEntity<ClientResponse> createClient(@RequestBody ClientRequest request) {
        Client created = clientUseCase.create(request.client());
        return ResponseEntity.ok(ClientResponse.of(created));
    }

    @PutMapping("/client/{id}")
    public ResponseEntity<ClientResponse> updateClient(@PathVariable Long id, @RequestBody ClientRequest request) {
        Client updated = clientUseCase.update(request.client());
        return ResponseEntity.ok(ClientResponse.of(updated));
    }

    @DeleteMapping("/client/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        clientUseCase.delete(id);
        return ResponseEntity.ok().build();
    }
}
