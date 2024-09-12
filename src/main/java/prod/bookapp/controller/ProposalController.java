package prod.bookapp.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import prod.bookapp.configuration.PaginationUtils;
import prod.bookapp.dto.ProposalCreateDTO;
import prod.bookapp.dto.ProposalCreateWVenueDTO;
import prod.bookapp.dto.ProposalUpdateDTO;
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

    @GetMapping
    public ResponseEntity<ApiResponse<Object>> getAll(Pageable pageable) {
        var result = new PagedModel<>(PaginationUtils.paginate(
                proposalService.getAll(getAuth()), pageable
        ));
        return ResultWrapper.getResponse(result);
    }

    @PostMapping("/update")
    public ResponseEntity<ApiResponse<Object>> update(
            @RequestBody ProposalUpdateDTO proposalUpdateDTO
    ) {
        var result = proposalService.update(proposalUpdateDTO, getAuth());
        return ResultWrapper.getResponse(result);
    }

    @PostMapping("/delete")
    public ResponseEntity<ApiResponse<Object>> delete(
            @RequestParam Long id
    ){
        var result = proposalService.delete(id, getAuth());
        return ResultWrapper.getResponse(result);
    }

    @GetMapping("{id}")
    public ResponseEntity<ApiResponse<Object>> getById(
            @PathVariable Long id
    ) {
        var result = proposalService.getById(id, getAuth());
        return ResultWrapper.getResponse(result);
    }
}
