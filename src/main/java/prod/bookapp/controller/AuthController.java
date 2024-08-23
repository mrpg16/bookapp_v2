package prod.bookapp.controller;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private Authentication getAuth() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @GetMapping("/isAuthenticated")
    public String isAuthenticated() {
        return getAuth().toString();
    }

    @GetMapping("/type")
    public String getAuthType() {
        Authentication auth = getAuth();
        if (auth == null) {
            return "not authenticated";
        }
        if (auth instanceof OAuth2AuthenticationToken) {
            return "oauth2";
        }
        if (auth instanceof UsernamePasswordAuthenticationToken) {
            return "basic";
        }
        return "not authenticated";
    }
}
//TODO HANDLE WRONG PASSWORD AUTH