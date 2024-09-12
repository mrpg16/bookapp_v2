package prod.bookapp.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import prod.bookapp.configuration.PaginationUtils;
import prod.bookapp.dto.TimeSlotDTO;
import prod.bookapp.dto.customResponse.FreeSlotsSearchDTO;
import prod.bookapp.enums.ResultWrapper;
import prod.bookapp.service.TimeSlotService;
import prod.bookapp.wraper.ApiResponse;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/timeslot")
public class TimeSlotController {
    private final TimeSlotService timeSlotService;

    public TimeSlotController(TimeSlotService timeSlotService) {
        this.timeSlotService = timeSlotService;
    }

    private Authentication getAuth() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @GetMapping("/self/all")
    public ResponseEntity<ApiResponse<Object>> getAllSelfSlots(
            @RequestParam LocalDate dateFrom,
            @RequestParam LocalDate dateTo,
            Pageable pageable
    ) {
        if (dateFrom.equals(dateTo)) {
            List<TimeSlotDTO> slots = timeSlotService.getAllSelfSlotsByDate(dateFrom, getAuth());
            var result = new PagedModel<>(PaginationUtils.paginate(slots, pageable));
            return ResultWrapper.getResponse(result);
        }
        List<TimeSlotDTO> slots = timeSlotService.getAllSelfSlotsByDateBetween(dateFrom, dateTo, getAuth());
        var result = new PagedModel<>(PaginationUtils.paginate(slots, pageable));
        return ResultWrapper.getResponse(result);
    }

    @GetMapping("/free")
    public ResponseEntity<ApiResponse<Object>> getFreeSlotsByDateAndWorkerAndProposal(
            @RequestParam LocalDate dateFrom,
            @RequestParam LocalDate dateTo,
            @RequestParam Long workerId,
            @RequestParam Long proposalId,
            Pageable pageable
    ) {
        Page<TimeSlotDTO> paginatedSlots;
        List<TimeSlotDTO> slots;
        if (dateFrom.equals(dateTo)) {
            slots = timeSlotService.getAllFreeSlotsByDateAndWorkerIdAndProposalId(dateFrom, workerId, proposalId);
        } else {
            slots = timeSlotService.getAllFreeSlotsByDateBetweenAndWorkerIdAndProposalId(dateFrom, dateTo, workerId, proposalId);
        }
        paginatedSlots = PaginationUtils.paginate(slots, pageable);
        var result = new FreeSlotsSearchDTO(new PagedModel<>(paginatedSlots), workerId, proposalId);
        return ResultWrapper.getResponse(result);
    }

}