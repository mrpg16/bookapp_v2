package prod.bookapp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.*;
import prod.bookapp.dto.UserAuthRequestDTO;
import prod.bookapp.dto.UserAuthRequestRefreshDTO;
import prod.bookapp.enums.ResultWrapper;
import prod.bookapp.service.AuthService;
import prod.bookapp.wraper.ApiResponse;

@RestController
@RequestMapping("/auth")
public class AuthController {


    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }


    private Authentication getAuth() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Object>> createAuthenticationToken(
            @RequestBody UserAuthRequestDTO authRequest
    ) throws AuthenticationException {
        var result = authService.getToken(authRequest);
        return ResultWrapper.getResponse(result);

    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<Object>> refreshAuthenticationToken(
            @RequestBody UserAuthRequestRefreshDTO authRequest
    ) {
        var result = authService.refreshToken(authRequest);
        return ResultWrapper.getResponse(result);
    }

    @GetMapping("/isAuthenticated")
    public ResponseEntity<ApiResponse<Object>> isAuthenticated() {
        var result = getAuth();
        if (!result.isAuthenticated() || result.getPrincipal().equals("anonymousUser")) {
            return ResultWrapper.getResponse(false);
        }
        return ResultWrapper.getResponse(true);
    }

    @GetMapping("/type")
    public ResponseEntity<ApiResponse<Object>> getAuthType() {
        Authentication auth = getAuth();
        if (auth == null) {
            var result = "not authenticated";
            return ResultWrapper.getResponse(result);
        }
        if (auth instanceof OAuth2AuthenticationToken) {
            var result = "oauth2";
            return ResultWrapper.getResponse(result);
        }
        if (auth instanceof UsernamePasswordAuthenticationToken) {
            var result = "jwt";
            return ResultWrapper.getResponse(result);
        }
        var result = "not authenticated";
        return ResultWrapper.getResponse(result);
    }
}
