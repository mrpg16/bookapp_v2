package prod.bookapp.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import prod.bookapp.configuration.PaginationUtils;
import prod.bookapp.dto.VenueCreateDTO;
import prod.bookapp.dto.VenueViewDTO;
import prod.bookapp.service.VenueService;

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
    public String create(
            @RequestBody VenueCreateDTO venueCreateDTO
    ) {
        return venueService.create(venueCreateDTO, getAuth());
    }

    @PostMapping("/update")
    public String update(
            @RequestBody VenueViewDTO venueViewDTO
    ) {
        return venueService.update(venueViewDTO, getAuth());
    }

    @PostMapping("/delete")
    public String delete(
            @RequestParam Long id
    ) {
        return venueService.delete(id, getAuth());
    }

    @GetMapping
    public PagedModel<?> getAllVenues(Pageable pageable) {
        return new PagedModel<>(PaginationUtils.paginate(
                venueService.getAllVenues(getAuth()), pageable
        ));
    }


}
