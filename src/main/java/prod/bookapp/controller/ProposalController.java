package prod.bookapp.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import prod.bookapp.configuration.PaginationUtils;
import prod.bookapp.dto.*;
import prod.bookapp.dto.converter.UserViewDTOConverter;
import prod.bookapp.entity.User;
import prod.bookapp.service.ProposalService;
import prod.bookapp.service.UserService;
import prod.bookapp.wraper.ApiResponse;
import prod.bookapp.wraper.ResultWrapper;

import java.util.List;

@RestController
@RequestMapping()
public class ProposalController {
    private final ProposalService proposalService;
    private final UserService userService;
    private final UserViewDTOConverter userViewDTOConverter;

    public ProposalController(ProposalService proposalService, UserService userService, UserViewDTOConverter userViewDTOConverter) {
        this.proposalService = proposalService;
        this.userService = userService;
        this.userViewDTOConverter = userViewDTOConverter;
    }

    private Authentication getAuth() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @PostMapping("/proposal")
    public ResponseEntity<ApiResponse<Object>> create(
            @RequestBody List<ProposalCreateDTO> proposalCreateDTOs
    ) {
        var result = proposalService.createAll(proposalCreateDTOs, getAuth());
        return ResultWrapper.getResponse(result);
    }

    @PostMapping("/proposal/wVenue")
    public ResponseEntity<ApiResponse<Object>> createWithVenue(
            @RequestBody List<ProposalCreateWVenueDTO> proposalCreateWVenueDTOs
    ) {
        var result = proposalService.createAllWithVenue(proposalCreateWVenueDTOs, getAuth());
        return ResultWrapper.getResponse(result);
    }

    @GetMapping("/proposal")
    public ResponseEntity<ApiResponse<Object>> getAll(Pageable pageable) {
        var result = new PagedModel<>(PaginationUtils.paginate(
                proposalService.getAll(getAuth()), pageable
        ));
        return ResultWrapper.getResponse(result);
    }

    @PostMapping("/proposal/update")
    public ResponseEntity<ApiResponse<Object>> update(
            @RequestBody ProposalUpdateDTO proposalUpdateDTO
    ) {
        var result = proposalService.update(proposalUpdateDTO, getAuth());
        return ResultWrapper.getResponse(result);
    }

    @PostMapping("/proposal/delete")
    public ResponseEntity<ApiResponse<Object>> delete(
            @RequestParam Long id
    ) {
        var result = proposalService.delete(id, getAuth());
        return ResultWrapper.getResponse(result);
    }

    @GetMapping("/proposal/{id}")
    public ResponseEntity<ApiResponse<Object>> getById(
            @PathVariable Long id
    ) {
        var result = proposalService.getById(id, getAuth());
        return ResultWrapper.getResponse(result);
    }

    @GetMapping("/worker/{id}/proposal")
    public ResponseEntity<ApiResponse<Object>> getAllByWorkerId(
            @PathVariable Long id
            , Pageable pageable
    ) {
        List<ProposalViewDTO> props = proposalService.getAllByWorkerId(id);
        if (props.isEmpty()) {
            return ResultWrapper.getResponse("Error: proposal not found");
        }
        User worker = userService.getUserById(id);
        if (worker == null) {
            return ResultWrapper.getResponse("Error: worker not found");
        }
        var workerDTO = userViewDTOConverter.convertToUserViewDTO(worker);
        Page<ProposalViewDTO> propsPage = PaginationUtils.paginate(props, pageable);
        var result = new ProposalCustomerViewDTO(new PagedModel<>(propsPage), workerDTO);
        return ResultWrapper.getResponse(result);
    }

    @GetMapping("/worker/{workerId}/proposal/{propId}/venues")
    public ResponseEntity<ApiResponse<Object>> getAllVenuesByWorkerIdAndPropId(
            @PathVariable("workerId") long workerId
            , @PathVariable("propId") long propId
            , Pageable pageable
    ) {
        var result = new PagedModel<>(PaginationUtils.paginate(
                proposalService.getAllVenuesOfProposalByIdAndWorkerId(workerId, propId), pageable
        ));
        return ResultWrapper.getResponse(result);
    }
}
