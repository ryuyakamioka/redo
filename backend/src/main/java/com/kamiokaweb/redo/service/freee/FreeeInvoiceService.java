package com.kamiokaweb.redo.service.freee;

import com.kamiokaweb.redo.config.FreeeOAuthConfig;
import com.kamiokaweb.redo.model.company.Company;
import com.kamiokaweb.redo.model.company.CompanyId;
import com.kamiokaweb.redo.model.invoice.InvoiceEstimate;
import com.kamiokaweb.redo.repository.company.CompanyRepository;
import com.kamiokaweb.redo.usecase.InvoiceEstimateUseCase;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FreeeInvoiceService {

    private final FreeeOAuthService oauthService;
    private final FreeeOAuthConfig config;
    private final InvoiceEstimateUseCase invoiceEstimateUseCase;
    private final CompanyRepository companyRepository;
    private final RestTemplate restTemplate;
    private static final String FREEE_API_BASE_URL = "https://api.freee.co.jp/iv";

    public FreeeInvoiceService(
            FreeeOAuthService oauthService,
            FreeeOAuthConfig config,
            InvoiceEstimateUseCase invoiceEstimateUseCase,
            CompanyRepository companyRepository
    ) {
        this.oauthService = oauthService;
        this.config = config;
        this.invoiceEstimateUseCase = invoiceEstimateUseCase;
        this.companyRepository = companyRepository;
        this.restTemplate = new RestTemplate();
    }

    /**
     * freee APIに複数の請求書を送信
     *
     * @param billingMonth 請求年月（例: "202512"）
     * @param companyIds 請求書を作成する会社IDのリスト
     * @return 作成された請求書IDのリスト
     */
    public List<Long> createInvoices(String billingMonth, List<Long> companyIds) {
        return companyIds.stream()
                .map(companyId -> createInvoice(billingMonth, companyId))
                .toList();
    }

    /**
     * freee APIに請求書を送信
     *
     * @param billingMonth 請求年月（例: "202512"）
     * @param companyId 会社ID
     * @return 作成された請求書のID
     */
    private Long createInvoice(String billingMonth, Long companyId) {
        String accessToken = oauthService.getAccessToken();
        if (accessToken == null) {
            throw new RuntimeException("freeeアクセストークンが取得できません。先に認証を完了してください。");
        }

        Long freeeCompanyId = config.getCompanyId();
        if (freeeCompanyId == null) {
            throw new RuntimeException("freee事業所IDが設定されていません。");
        }

        // CompanyRepositoryからfreeePartnerIdを取得
        Company company = companyRepository.get(new CompanyId(companyId))
                .orElseThrow(() -> new RuntimeException("会社が見つかりません: " + companyId));

        Long freeePartnerId = company.freeePartnerId();
        if (freeePartnerId == null) {
            throw new RuntimeException("freee取引先IDが設定されていません。会社ID: " + companyId);
        }

        // getEstimatesを使用して請求データを取得
        List<InvoiceEstimate> estimates = invoiceEstimateUseCase.getEstimates(billingMonth, companyId);

        if (estimates.isEmpty()) {
            throw new RuntimeException("指定された請求月のデータが見つかりません");
        }

        // 最初の請求予定を使用（1社のみを想定）
        InvoiceEstimate estimate = estimates.get(0);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("company_id", freeeCompanyId);
        requestBody.put("partner_id", freeePartnerId);
        requestBody.put("issue_date", LocalDate.now().toString());

        // 必須パラメータを追加
        requestBody.put("billing_date", LocalDate.now().toString()); // 請求日
        requestBody.put("tax_entry_method", "out"); // 外税
        requestBody.put("tax_fraction", "round"); // 四捨五入
        requestBody.put("withholding_tax_entry_method", "out"); // 源泉徴収税を含めない
        requestBody.put("partner_title", "御中"); // 取引先名（敬称）

        // InvoiceEstimateItemをfreee APIの"lines"形式に変換
        List<Map<String, Object>> lines = estimate.items().stream()
                .map(item -> {
                    Map<String, Object> line = new HashMap<>();
                    line.put("type", "item");
                    line.put("description", item.description());
                    line.put("quantity", item.quantity());
                    // unit_priceは文字列で送信
                    line.put("unit_price", item.unitPrice().toString());
                    line.put("tax_rate", 10);
                    return line;
                })
                .toList();

        requestBody.put("lines", lines);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        try {
            @SuppressWarnings("rawtypes")
            ResponseEntity<Map> response = restTemplate.postForEntity(
                    FREEE_API_BASE_URL + "/invoices",
                    request,
                    Map.class
            );

            if (response.getStatusCode() == HttpStatus.CREATED && response.getBody() != null) {
                @SuppressWarnings("unchecked")
                Map<String, Object> body = response.getBody();
                @SuppressWarnings("unchecked")
                Map<String, Object> invoice = (Map<String, Object>) body.get("invoice");
                return ((Number) invoice.get("id")).longValue();
            }

            throw new RuntimeException("freee請求書の作成に失敗しました");
        } catch (Exception e) {
            throw new RuntimeException("freee API呼び出しエラー: " + e.getMessage(), e);
        }
    }
}
