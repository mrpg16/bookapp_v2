package prod.bookapp.configuration;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;
import prod.bookapp.entity.User;
import prod.bookapp.jwt.JwtUtil;
import prod.bookapp.repository.UserRepository;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final OAuth2AuthorizedClientService authorizedClientService;

    public CustomAuthenticationSuccessHandler(UserRepository userRepository, JwtUtil jwtUtil, OAuth2AuthorizedClientService authorizedClientService) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.authorizedClientService = authorizedClientService;
    }

    @Override
    @Transactional
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;

        OAuth2AuthorizedClient authorizedClient = authorizedClientService.loadAuthorizedClient(
                oauthToken.getAuthorizedClientRegistrationId(),
                oauthToken.getName()
        );

        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        String accessToken = authorizedClient.getAccessToken().getTokenValue();
        String refreshToken = authorizedClient.getRefreshToken() != null ? authorizedClient.getRefreshToken().getTokenValue() : null;
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
            user.setOauth2AccessToken(accessToken);
            user.setOauth2RefreshToken(refreshToken);
            userRepository.save(user);
        } else {
            user.setLastLogin(LocalDateTime.now());
            user.setFirstName(name);
            user.setEmailVerified(emailVerified);
            user.setProvider(provider);
            user.setIp(request.getRemoteAddr());
            user.setOauth2AccessToken(accessToken);
            user.setOauth2RefreshToken(refreshToken);
            userRepository.save(user);
        }
        String jwtToken = jwtUtil.generateToken(email);
        String jwtRefreshToken = jwtUtil.generateRefreshToken(email);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Authorization", "Bearer " + jwtToken);
        response.getWriter().write("{\"accessToken\": \"" + jwtToken + "\", \"refreshToken\": \"" + jwtRefreshToken + "\"}");
        response.getWriter().flush();
    }
}
