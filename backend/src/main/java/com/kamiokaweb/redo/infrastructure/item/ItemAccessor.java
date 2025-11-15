package com.kamiokaweb.redo.infrastructure.item;

import org.springframework.data.repository.CrudRepository;

public interface ItemAccessor extends CrudRepository<ItemDto, Long> {
}
