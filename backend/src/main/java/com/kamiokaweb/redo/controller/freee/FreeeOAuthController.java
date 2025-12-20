package com.kamiokaweb.redo.controller.freee;

import com.kamiokaweb.redo.service.freee.FreeeOAuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/freee")
public class FreeeOAuthController {

    private final FreeeOAuthService freeeOAuthService;

    public FreeeOAuthController(FreeeOAuthService freeeOAuthService) {
        this.freeeOAuthService = freeeOAuthService;
    }

    /**
     * 認証URL取得
     */
    @GetMapping("/auth-url")
    public ResponseEntity<Map<String, String>> getAuthUrl() {
        String authUrl = freeeOAuthService.generateAuthorizationUrl();
        return ResponseEntity.ok(Map.of("authUrl", authUrl));
    }

    /**
     * コールバック処理
     */
    @GetMapping("/callback")
    public ResponseEntity<Map<String, String>> callback(@RequestParam String code) {
        try {
            freeeOAuthService.exchangeCodeForToken(code);
            return ResponseEntity.ok(Map.of("status", "success", "message", "認証に成功しました"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("status", "error", "message", "認証に失敗しました: " + e.getMessage()));
        }
    }

    /**
     * 認証状態確認
     */
    @GetMapping("/status")
    public ResponseEntity<Map<String, Boolean>> getAuthStatus() {
        boolean hasValidToken = freeeOAuthService.hasValidToken();
        return ResponseEntity.ok(Map.of("authenticated", hasValidToken));
    }
}
