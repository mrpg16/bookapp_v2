package prod.bookapp.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import prod.bookapp.configuration.PaginationUtils;
import prod.bookapp.dto.AppointmentBookDTO;
import prod.bookapp.service.AppointmentService;
import prod.bookapp.service.BookingService;

import java.time.LocalDate;

@RestController
@RequestMapping("/appointment")
public class AppointmentController {
    private final BookingService bookingService;
    private final AppointmentService appointmentService;

    public AppointmentController(BookingService bookingService, AppointmentService appointmentService) {
        this.bookingService = bookingService;
        this.appointmentService = appointmentService;
    }

    private Authentication getAuth() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @PostMapping("/book")
    public String bookAppointment(
            @RequestBody AppointmentBookDTO appointment
    ) {
        return bookingService.book(appointment, getAuth());
    }

    @GetMapping("/worker")
    public PagedModel<?> getAppointmentsAsWorkerByDates(
            @RequestParam LocalDate dateFrom,
            @RequestParam LocalDate dateTo,
            Pageable pageable
    ) {
        return new PagedModel<>(PaginationUtils.paginate(
                appointmentService.getAppointmentsByWorkerAndDateBetween(getAuth(), dateFrom, dateTo), pageable
        ));
    }

    @GetMapping("/client")
    public PagedModel<?> getAppointmentsAsClientByDates(
            @RequestParam LocalDate dateFrom,
            @RequestParam LocalDate dateTo,
            Pageable pageable
    ) {
        return new PagedModel<>(PaginationUtils.paginate(
                appointmentService.getAppointmentsByClientAndDateBetween(getAuth(), dateFrom, dateTo), pageable
        ));
    }

    @PostMapping("/worker/confirm")
    public String confirm(
            @RequestParam(required = true) Long appId
    ) {
        return appointmentService.confirm(appId, getAuth());
    }

    @PostMapping("/worker/reject")
    public String reject(
            @RequestParam(required = true) Long appId
    ) {
        return appointmentService.reject(appId, getAuth());
    }


}
