package com.kamiokaweb.redo.controller.freee;

import com.kamiokaweb.redo.service.freee.FreeeInvoiceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/freee/invoices")
public class FreeeInvoiceController {

    private final FreeeInvoiceService freeeInvoiceService;

    public FreeeInvoiceController(FreeeInvoiceService freeeInvoiceService) {
        this.freeeInvoiceService = freeeInvoiceService;
    }

    /**
     * freeeに請求書を送信
     */
    @PostMapping("/send")
    public ResponseEntity<Map<String, Object>> sendInvoice(@RequestBody SendInvoiceRequest request) {
        try {
            Long invoiceId = freeeInvoiceService.createInvoice(
                    request.billingMonth()
            );

            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "freeeに請求書を送信しました",
                    "invoiceId", invoiceId
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "message", "請求書の送信に失敗しました: " + e.getMessage()
            ));
        }
    }

    public record SendInvoiceRequest(
            String billingMonth
    ) {}
}
