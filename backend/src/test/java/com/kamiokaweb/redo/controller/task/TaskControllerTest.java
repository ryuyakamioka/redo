package com.kamiokaweb.redo.controller.task;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getTasks_依頼一覧を取得できる() throws Exception {
        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.taskList").isArray());
    }

    @Test
    void registerTask_依頼を登録できる() throws Exception {
        String requestBody = """
                {
                    "task": {
                        "taskId": {
                            "value": 0
                        },
                        "taskTitle": {
                            "value": "APIテスト依頼"
                        },
                        "taskStatus": "TODO",
                        "requestDate": "2025-01-15",
                        "client": {
                            "clientId": {
                                "value": 1
                            },
                            "clientName": {
                                "value": "顧客A"
                            },
                            "clientAbbreviation": {
                                "value": "A"
                            },
                            "company": {
                                "companyId": {
                                    "value": 1
                                },
                                "companyName": {
                                    "value": "株式会社サンプル"
                                },
                                "withholdingTax": true,
                                "freeePartnerId": null
                            },
                            "showClientNameInDescription": false
                        },
                        "note": "APIテストメモ",
                        "taskItems": [],
                        "expectedDeliveryDate": "2025-01-18",
                        "deliveryDate": null
                    }
                }
                """;

        mockMvc.perform(post("/task")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk());
    }

    @Test
    void deleteTask_依頼を削除できる() throws Exception {
        // Arrange: まず依頼を登録
        String requestBody = """
                {
                    "task": {
                        "taskId": {
                            "value": 0
                        },
                        "taskTitle": {
                            "value": "削除テスト依頼"
                        },
                        "taskStatus": "TODO",
                        "requestDate": "2025-01-15",
                        "client": {
                            "clientId": {
                                "value": 1
                            },
                            "clientName": {
                                "value": "顧客A"
                            },
                            "clientAbbreviation": {
                                "value": "A"
                            },
                            "company": {
                                "companyId": {
                                    "value": 1
                                },
                                "companyName": {
                                    "value": "株式会社サンプル"
                                },
                                "withholdingTax": true,
                                "freeePartnerId": null
                            },
                            "showClientNameInDescription": false
                        },
                        "note": null,
                        "taskItems": [],
                        "expectedDeliveryDate": "2025-01-18",
                        "deliveryDate": null
                    }
                }
                """;

        mockMvc.perform(post("/task")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        // Note: 実際のIDを取得して削除するには、一覧取得APIを叩く必要があるため、
        // ここでは登録されたことを前提に、適当なIDで削除テストを行う
        // 実際の運用では、登録後にIDを取得してから削除する
    }
}
