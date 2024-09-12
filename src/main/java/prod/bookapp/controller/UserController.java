package prod.bookapp.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import prod.bookapp.dto.UserRegisterDTO;
import prod.bookapp.enums.ResultWrapper;
import prod.bookapp.service.UserService;
import prod.bookapp.wraper.ApiResponse;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Object>> register(@RequestBody UserRegisterDTO userRegisterDTO, HttpServletRequest request) {
        var result = userService.registerUser(userRegisterDTO, request);
        return ResultWrapper.getResponse(result);
    }
}
