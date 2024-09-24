package prod.bookapp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import prod.bookapp.dto.OnboardingCreateDTO;
import prod.bookapp.service.OnboardingService;
import prod.bookapp.wraper.ApiResponse;

@RestController
@RequestMapping("/onboard")
public class OnboardingController {
    private final OnboardingService onboardingService;

    public OnboardingController(OnboardingService onboardingService) {
        this.onboardingService = onboardingService;
    }

    private Authentication getAuth() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Object>> createEntities(
            @RequestBody OnboardingCreateDTO onboardingCreateDTO
    ) {
        var result = onboardingService.create(onboardingCreateDTO, getAuth());
        return ResponseEntity.ok(new ApiResponse<>(result));
    }

}
