package prod.bookapp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import prod.bookapp.dto.ProposalCreateDTO;
import prod.bookapp.dto.ProposalCreateWVenueDTO;
import prod.bookapp.enums.ResultWrapper;
import prod.bookapp.service.ProposalService;
import prod.bookapp.wraper.ApiResponse;

@RestController
@RequestMapping("/proposal")
public class ProposalController {
    private final ProposalService proposalService;

    public ProposalController(ProposalService proposalService) {
        this.proposalService = proposalService;
    }

    private Authentication getAuth() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @PostMapping()
    public ResponseEntity<ApiResponse<Object>> create(
            @RequestBody ProposalCreateDTO proposalCreateDTO
    ) {
        var result = proposalService.create(proposalCreateDTO, getAuth());
        return ResultWrapper.getResponse(result);
    }

    @PostMapping("/wVenue")
    public ResponseEntity<ApiResponse<Object>> createWithVenue(
            @RequestBody ProposalCreateWVenueDTO proposalCreateWVenueDTO
    ) {
        var result = proposalService.createWithVenue(proposalCreateWVenueDTO, getAuth());
        return ResultWrapper.getResponse(result);
    }
}
