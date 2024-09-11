package prod.bookapp.configuration;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;
import prod.bookapp.entity.User;
import prod.bookapp.repository.UserRepository;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final HttpSessionRequestCache requestCache = new HttpSessionRequestCache();

    public CustomAuthenticationSuccessHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        boolean emailVerified = Boolean.TRUE.equals(oAuth2User.getAttribute("email_verified"));
        String provider = oauthToken.getAuthorizedClientRegistrationId();

        User user = userRepository.findByEmail(email);
        if (user == null) {
            user = new User();
            user.setEmail(email);
            user.setFirstName(name);
            user.setProvider(provider);
            user.setFirstLogin(LocalDateTime.now());
            user.setLastLogin(LocalDateTime.now());
            user.setEmailVerified(emailVerified);
            user.setIp(request.getRemoteAddr());
            userRepository.save(user);
        } else {
            user.setLastLogin(LocalDateTime.now());
            user.setFirstName(name);
            user.setEmailVerified(emailVerified);
            user.setProvider(provider);
            user.setIp(request.getRemoteAddr());
            userRepository.save(user);
        }
        SavedRequest savedRequest = requestCache.getRequest(request, response);
//        String targetUrl = savedRequest != null ? savedRequest.getRedirectUrl() : "/home";
        String targetUrl = "http://localhost:5173/";
        response.sendRedirect(targetUrl);
//TODO change redirect url domain to frontend not backend!
    }
}
