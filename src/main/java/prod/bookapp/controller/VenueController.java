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
import prod.bookapp.wraper.ResultWrapper;
import prod.bookapp.service.VenueService;
import prod.bookapp.wraper.ApiResponse;

import java.util.List;
import java.util.Objects;

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
            @RequestBody List<VenueCreateDTO> venueCreateDTOs
    ) {
        var result = venueService.createAll(venueCreateDTOs, getAuth());
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

    @GetMapping("/type/{online}")
    public ResponseEntity<ApiResponse<Object>> getAllOnlineVenues(
            Pageable pageable,
            @PathVariable("online") String online) {
        if (Objects.equals(online, "online")) {
            var result = new PagedModel<>(PaginationUtils.paginate(
                    venueService.getAllOnlineVenues(getAuth()), pageable
            ));
            return ResultWrapper.getResponse(result);
        }
        if (Objects.equals(online, "offline")) {
            var result = new PagedModel<>(PaginationUtils.paginate(
                    venueService.getAllOfflineVenues(getAuth()), pageable
            ));
            return ResultWrapper.getResponse(result);
        }
        return ResultWrapper.getResponse("Error: path not found");
    }

    @GetMapping("{id}")
    public ResponseEntity<ApiResponse<Object>> getById(
            @PathVariable Long id
    ) {
        var result = venueService.getById(id, getAuth());
        return ResultWrapper.getResponse(result);
    }

}
