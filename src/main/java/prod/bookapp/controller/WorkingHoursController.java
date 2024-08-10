package prod.bookapp.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import prod.bookapp.dto.WorkingHoursCreateDTO;
import prod.bookapp.service.WorkingHoursService;

import java.util.List;

@RestController
@RequestMapping("/working")
public class WorkingHoursController {
    private final WorkingHoursService workingHoursService;

    public WorkingHoursController(WorkingHoursService workingHoursService) {
        this.workingHoursService = workingHoursService;
    }

    private Authentication getAuth() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @PostMapping
    public String create(
            @RequestBody List<WorkingHoursCreateDTO> whDTO
    ) {
        return workingHoursService.createOrUpdate(whDTO, getAuth());
    }
}
