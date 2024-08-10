package prod.bookapp.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import prod.bookapp.configuration.PaginationUtils;
import prod.bookapp.dto.TimeSlotDTO;
import prod.bookapp.dto.customResponse.FreeSlotsSearchDTO;
import prod.bookapp.service.TimeSlotService;

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
    public PagedModel<?> getAllSelfSlots(
            @RequestParam LocalDate dateFrom,
            @RequestParam LocalDate dateTo,
            Pageable pageable
    ) {
        if (dateFrom.equals(dateTo)) {
            List<TimeSlotDTO> slots = timeSlotService.getAllSelfSlotsByDate(dateFrom, getAuth());
            return new PagedModel<>(PaginationUtils.paginate(slots, pageable));
        }
        List<TimeSlotDTO> slots = timeSlotService.getAllSelfSlotsByDateBetween(dateFrom, dateTo, getAuth());
        return new PagedModel<>(PaginationUtils.paginate(slots, pageable));
    }

    @GetMapping("/free")
    public FreeSlotsSearchDTO getFreeSlotsByDateAndWorkerAndProposal(
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
        return new FreeSlotsSearchDTO(new PagedModel<>(paginatedSlots), workerId, proposalId);
    }

}