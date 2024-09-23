package prod.bookapp.configuration;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import prod.bookapp.entity.User;
import prod.bookapp.jwt.JwtUtil;
import prod.bookapp.repository.UserRepository;

import java.time.LocalDateTime;

@Component
public class CustomAuthenticationSuccessHandlerBasic extends SimpleUrlAuthenticationSuccessHandler {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public CustomAuthenticationSuccessHandlerBasic(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        User authUser = (User) authentication.getPrincipal();
        User user = userRepository.findByEmail(authUser.getEmail());
        if (user != null) {
            user.setLastLogin(LocalDateTime.now());
            user.setIp(request.getRemoteAddr());
            userRepository.save(user);
        }
        String jwtToken = jwtUtil.generateToken(authUser.getUsername());

        response.setHeader("Authorization", "Bearer " + jwtToken);
    }
}