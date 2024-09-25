package prod.bookapp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;
import prod.bookapp.dto.UserAuthRequestDTO;
import prod.bookapp.dto.UserAuthRequestRefreshDTO;
import prod.bookapp.service.AuthService;
import prod.bookapp.wraper.ApiResponse;
import prod.bookapp.wraper.ResultWrapper;

@RestController
@RequestMapping("/auth")
public class AuthController {


    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
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

    @PostMapping("/check")
    public ResponseEntity<ApiResponse<Object>> checkAuthenticationToken(
            @RequestParam String token
    ) {
        var result = authService.isValidToken(token);
        return ResultWrapper.getResponse(result);
    }

}
