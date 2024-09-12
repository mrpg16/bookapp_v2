package prod.bookapp.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import prod.bookapp.dto.UserRegisterDTO;
import prod.bookapp.entity.User;
import prod.bookapp.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Map<String, Object> getUserAttr(@AuthenticationPrincipal OAuth2User principal) {
        return Collections.singletonMap("name", principal.getAttributes());
    }

    @Transactional
    public String registerUser(UserRegisterDTO userRegisterDTO, HttpServletRequest request) {
        var email = userRegisterDTO.getEmail();
        var password = userRegisterDTO.getPassword();
        if (email.isEmpty() || password.isEmpty()) {
            return "Error: Invalid email or password";
        }
        User user = userRepository.findByEmailAndProvider(email, "basic");
        if (user != null) {
            return ("Error: User " + email + " already exists");
        }
        User newUser = new User();
        newUser.setEmail(email);
        newUser.setPassword(passwordEncoder.encode(password));
        newUser.setProvider("basic");
        newUser.setFirstLogin(LocalDateTime.now());
        newUser.setLastLogin(LocalDateTime.now());
        newUser.setEmailVerified(false);
        newUser.setIp(request.getRemoteAddr());
        newUser.setFirstName(userRegisterDTO.getFirstName());
        newUser.setLastName(userRegisterDTO.getLastName());
        userRepository.save(newUser);
        return newUser.getId().toString();
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }


}
