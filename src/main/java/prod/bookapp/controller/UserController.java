package prod.bookapp.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import prod.bookapp.dto.UserRegisterDTO;
import prod.bookapp.service.UserService;
import prod.bookapp.wraper.ApiResponse;
import prod.bookapp.wraper.ResultWrapper;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    private Authentication getAuth() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Object>> register(@RequestBody UserRegisterDTO userRegisterDTO, HttpServletRequest request) {
        var result = userService.registerUser(userRegisterDTO, request);
        return ResultWrapper.getResponse(result);
    }

    @GetMapping("/currency")
    public ResponseEntity<ApiResponse<Object>> getCurrency() {
        var result = userService.getUsersCurrency(getAuth());
        return ResultWrapper.getResponse(result);
    }
}
