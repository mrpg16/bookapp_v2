package prod.bookapp.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import prod.bookapp.dto.ProposalCreateDTO;
import prod.bookapp.dto.ProposalCreateWVenueDTO;
import prod.bookapp.service.ProposalService;

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
    public String create(
            @RequestBody ProposalCreateDTO proposalCreateDTO
    ) {
        return proposalService.create(proposalCreateDTO, getAuth());
    }

    @PostMapping("/wVenue")
    public String createWithVenue(
            @RequestBody ProposalCreateWVenueDTO proposalCreateWVenueDTO
            ) {
        return proposalService.createWithVenue(proposalCreateWVenueDTO, getAuth());
    }
}
