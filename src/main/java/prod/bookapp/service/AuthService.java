package prod.bookapp.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import prod.bookapp.dto.UserAuthRequestDTO;
import prod.bookapp.dto.UserAuthRequestRefreshDTO;
import prod.bookapp.dto.UserAuthResponseDTO;
import prod.bookapp.dto.UserAuthResponseRefreshDTO;
import prod.bookapp.jwt.JwtUtil;

@Service
public class AuthService {
    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    public AuthService(JwtUtil jwtUtil, UserService userService, AuthenticationManager authenticationManager) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    public UserAuthResponseDTO getToken(UserAuthRequestDTO authRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            return null;
        }

        var userEmail = userService.getUserByEmail(authRequest.getEmail()).getUsername();
        var accessToken = jwtUtil.generateToken(userEmail);
        var refreshToken = jwtUtil.generateRefreshToken(authRequest.getEmail());
        return new UserAuthResponseDTO(accessToken, refreshToken);
    }

    public UserAuthResponseRefreshDTO refreshToken(UserAuthRequestRefreshDTO userAuthRequestRefreshDTO) {
            if (jwtUtil.isTokenExpired(userAuthRequestRefreshDTO.getRefreshToken())) {
                return null;
            }
            var username = jwtUtil.getUsernameFromToken(userAuthRequestRefreshDTO.getRefreshToken());
            var accessToken = jwtUtil.generateToken(username);
            return new UserAuthResponseRefreshDTO(accessToken);
    }


}
