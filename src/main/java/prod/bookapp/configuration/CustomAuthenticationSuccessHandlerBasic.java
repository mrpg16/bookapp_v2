package prod.bookapp.configuration;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;
import prod.bookapp.entity.User;
import prod.bookapp.repository.UserRepository;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class CustomAuthenticationSuccessHandlerBasic extends SimpleUrlAuthenticationSuccessHandler {
    private final UserRepository userRepository;
    private final HttpSessionRequestCache requestCache = new HttpSessionRequestCache();

    public CustomAuthenticationSuccessHandlerBasic(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        User authUser = (User) authentication.getPrincipal();
        User user = userRepository.findByEmail(authUser.getEmail());
        if (user != null) {
            user.setLastLogin(LocalDateTime.now());
            user.setIp(request.getRemoteAddr());
            userRepository.save(user);
        }
        SavedRequest savedRequest = requestCache.getRequest(request, response);
        String targetUrl = savedRequest != null ? savedRequest.getRedirectUrl() : "/home";
        response.sendRedirect(targetUrl);

    }
}