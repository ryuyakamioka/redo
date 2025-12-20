package com.kamiokaweb.redo.service.freee;

import com.kamiokaweb.redo.config.FreeeOAuthConfig;
import com.kamiokaweb.redo.infrastructure.freee.FreeeTokenAccessor;
import com.kamiokaweb.redo.infrastructure.freee.FreeeTokenDto;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriUtils;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Service
public class FreeeOAuthService {

    private final FreeeOAuthConfig config;
    private final FreeeTokenAccessor tokenAccessor;
    private final RestTemplate restTemplate;

    public FreeeOAuthService(FreeeOAuthConfig config, FreeeTokenAccessor tokenAccessor) {
        this.config = config;
        this.tokenAccessor = tokenAccessor;
        this.restTemplate = new RestTemplate();
    }

    /**
     * 認可URLを生成
     */
    public String generateAuthorizationUrl() {
        String state = UUID.randomUUID().toString();
        String encodedRedirectUri = UriUtils.encode(config.getRedirectUri(), StandardCharsets.UTF_8);
        return config.getAuthorizeUrl() +
                "?response_type=code" +
                "&client_id=" + config.getClientId() +
                "&redirect_uri=" + encodedRedirectUri +
                "&state=" + state +
                "&prompt=select_company";
    }

    /**
     * 認可コードからアクセストークンを取得
     */
    @Transactional
    public void exchangeCodeForToken(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", config.getClientId());
        params.add("client_secret", config.getClientSecret());
        params.add("code", code);
        params.add("redirect_uri", config.getRedirectUri());

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        @SuppressWarnings("rawtypes")
        ResponseEntity<Map> response = restTemplate.postForEntity(
                config.getTokenUrl(),
                request,
                Map.class
        );

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            @SuppressWarnings("unchecked")
            Map<String, Object> body = response.getBody();
            String accessToken = (String) body.get("access_token");
            String refreshToken = (String) body.get("refresh_token");
            String tokenType = (String) body.get("token_type");
            Integer expiresIn = (Integer) body.get("expires_in");

            LocalDateTime expiresAt = LocalDateTime.now().plusSeconds(expiresIn);

            // 既存のトークンを削除
            tokenAccessor.deleteAll();

            // 新しいトークンを保存
            FreeeTokenDto tokenDto = new FreeeTokenDto(accessToken, refreshToken, tokenType, expiresAt);
            tokenAccessor.save(tokenDto);
        }
    }

    /**
     * 現在のアクセストークンを取得
     */
    public String getAccessToken() {
        return tokenAccessor.findLatest()
                .map(FreeeTokenDto::accessToken)
                .orElse(null);
    }

    /**
     * トークンが有効かどうかを確認
     */
    public boolean hasValidToken() {
        return tokenAccessor.findLatest()
                .map(token -> token.expiresAt().isAfter(LocalDateTime.now()))
                .orElse(false);
    }
}
