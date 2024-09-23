package prod.bookapp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import prod.bookapp.dto.WorkingHoursCreateDTO;
import prod.bookapp.wraper.ResultWrapper;
import prod.bookapp.service.WorkingHoursService;
import prod.bookapp.wraper.ApiResponse;

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
    public ResponseEntity<ApiResponse<Object>> create(
            @RequestBody List<WorkingHoursCreateDTO> whDTO
    ) {
        var result = workingHoursService.createOrUpdate(whDTO, getAuth());
        return ResultWrapper.getResponse(result);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Object>> getAll() {
        var result = workingHoursService.getAll(getAuth());
        return ResultWrapper.getResponse(result);
    }
}
