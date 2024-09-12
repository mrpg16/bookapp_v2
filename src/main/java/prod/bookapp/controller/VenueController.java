package prod.bookapp.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import prod.bookapp.configuration.PaginationUtils;
import prod.bookapp.dto.VenueCreateDTO;
import prod.bookapp.dto.VenueViewDTO;
import prod.bookapp.enums.ResultWrapper;
import prod.bookapp.service.VenueService;
import prod.bookapp.wraper.ApiResponse;

@RestController
@RequestMapping("/venue")
public class VenueController {
    private final VenueService venueService;

    public VenueController(VenueService venueService) {
        this.venueService = venueService;
    }

    private Authentication getAuth() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Object>> create(
            @RequestBody VenueCreateDTO venueCreateDTO
    ) {
        var result = venueService.create(venueCreateDTO, getAuth());
        return ResultWrapper.getResponse(result);
    }

    @PostMapping("/update")
    public ResponseEntity<ApiResponse<Object>> update(
            @RequestBody VenueViewDTO venueViewDTO
    ) {
        var result = venueService.update(venueViewDTO, getAuth());
        return ResultWrapper.getResponse(result);
    }

    @PostMapping("/delete")
    public ResponseEntity<ApiResponse<Object>> delete(
            @RequestParam Long id
    ) {
        var result = venueService.delete(id, getAuth());
        return ResultWrapper.getResponse(result);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Object>> getAllVenues(Pageable pageable) {
        var result = new PagedModel<>(PaginationUtils.paginate(
                venueService.getAllVenues(getAuth()), pageable
        ));
        return ResultWrapper.getResponse(result);
    }


}
