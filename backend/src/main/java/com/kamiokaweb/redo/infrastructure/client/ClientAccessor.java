package com.kamiokaweb.redo.infrastructure.client;

import org.springframework.data.repository.CrudRepository;

public interface ClientAccessor extends CrudRepository<ClientDto, Long> {
}
