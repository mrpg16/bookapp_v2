package prod.bookapp.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import prod.bookapp.dto.UserCurrenciesViewDTO;
import prod.bookapp.dto.UserRegisterDTO;
import prod.bookapp.entity.User;
import prod.bookapp.enums.Enums;
import prod.bookapp.repository.UserRepository;

import java.time.LocalDateTime;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    private User getAuthUser(Authentication authentication) {
        return (User) authentication.getPrincipal();
    }

    @Transactional
    public String registerUser(UserRegisterDTO userRegisterDTO, HttpServletRequest request) {
        var email = userRegisterDTO.getEmail();
        var password = userRegisterDTO.getPassword();
        var currency = userRegisterDTO.getDefaultCurrency();
        if (email.isEmpty() || password.isEmpty()) {
            return "Error: Invalid email or password";
        }
        User user = userRepository.findByEmailAndProvider(email, "basic");
        if (user != null) {
            return ("Error: User " + email + " already exists");
        }
        if (currency != null) {
            if (!Enums.getCurrencies().contains(currency)) {
                return "Error: Invalid currency";
            }
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
        newUser.setDefaultCurrency(currency);
        userRepository.save(newUser);
        return newUser.getId().toString();
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public UserCurrenciesViewDTO getUsersCurrency(Authentication authentication) {
        var user = getAuthUser(authentication);
        var def = user.getDefaultCurrency();
        var aval = Enums.getCurrencies();
        return new UserCurrenciesViewDTO(def, aval);
    }


}
