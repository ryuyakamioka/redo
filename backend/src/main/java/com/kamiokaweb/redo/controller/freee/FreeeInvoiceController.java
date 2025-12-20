package com.kamiokaweb.redo.controller.freee;

import com.kamiokaweb.redo.service.freee.FreeeInvoiceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
            List<Long> invoiceIds = freeeInvoiceService.createInvoices(
                    request.billingMonth(),
                    request.companyIds()
            );

            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "freeeに請求書を送信しました",
                    "invoiceIds", invoiceIds,
                    "count", invoiceIds.size()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "message", "請求書の送信に失敗しました: " + e.getMessage()
            ));
        }
    }

    public record SendInvoiceRequest(
            String billingMonth,
            List<Long> companyIds
    ) {}
}
