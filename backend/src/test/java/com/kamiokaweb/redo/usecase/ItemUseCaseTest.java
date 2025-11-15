package com.kamiokaweb.redo.usecase;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ItemUseCaseTest {

    @Autowired
    private ItemUseCase itemUseCase;

    @Test
    void getList_品目一覧を取得できる() {
        // Act
        var items = itemUseCase.getList();

        // Assert
        assertThat(items).isNotNull();
        assertThat(items).isNotEmpty(); // マイグレーションでサンプルデータが入っている
    }
}
