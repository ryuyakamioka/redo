package com.kamiokaweb.redo.usecase;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ClientUseCaseTest {

    @Autowired
    private ClientUseCase clientUseCase;

    @Test
    void getList_依頼人一覧を取得できる() {
        // Act
        var clients = clientUseCase.getList();

        // Assert
        assertThat(clients).isNotNull();
        assertThat(clients).isNotEmpty(); // マイグレーションでサンプルデータが入っている
        assertThat(clients.get(0).company()).isNotNull();
    }
}
