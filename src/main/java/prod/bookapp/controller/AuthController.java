package prod.bookapp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import prod.bookapp.enums.ResultWrapper;
import prod.bookapp.wraper.ApiResponse;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private Authentication getAuth() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @GetMapping("/isAuthenticated")
    public ResponseEntity<ApiResponse<Object>> isAuthenticated() {
        var result = getAuth();
        System.out.println(result);
        System.out.println(result.isAuthenticated());
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
            var result = "basic";
            return ResultWrapper.getResponse(result);
        }
        var result = "not authenticated";
        return ResultWrapper.getResponse(result);
    }
}
//TODO HANDLE WRONG PASSWORD AUTH