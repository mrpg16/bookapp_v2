package prod.bookapp.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import prod.bookapp.configuration.PaginationUtils;
import prod.bookapp.dto.AppointmentBookDTO;
import prod.bookapp.enums.ResultWrapper;
import prod.bookapp.service.AppointmentService;
import prod.bookapp.service.BookingService;
import prod.bookapp.wraper.ApiResponse;

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
    public ResponseEntity<ApiResponse<Object>> bookAppointment(
            @RequestBody AppointmentBookDTO appointment
    ) {
        var result = bookingService.book(appointment, getAuth());
        return ResultWrapper.getResponse(result);
    }

    @GetMapping("/worker")
    public ResponseEntity<ApiResponse<Object>> getAppointmentsAsWorkerByDates(
            @RequestParam LocalDate dateFrom,
            @RequestParam LocalDate dateTo,
            Pageable pageable
    ) {
        var result = new PagedModel<>(PaginationUtils.paginate(
                appointmentService.getAppointmentsByWorkerAndDateBetween(getAuth(), dateFrom, dateTo), pageable
        ));
        return ResultWrapper.getResponse(result);
    }

    @GetMapping("/client")
    public ResponseEntity<ApiResponse<Object>> getAppointmentsAsClientByDates(
            @RequestParam LocalDate dateFrom,
            @RequestParam LocalDate dateTo,
            Pageable pageable
    ) {
        var result = new PagedModel<>(PaginationUtils.paginate(
                appointmentService.getAppointmentsByClientAndDateBetween(getAuth(), dateFrom, dateTo), pageable
        ));
        return ResultWrapper.getResponse(result);
    }

    @PostMapping("/worker/confirm")
    public ResponseEntity<ApiResponse<Object>> confirm(
            @RequestParam Long appId
    ) {
        var result = appointmentService.confirm(appId, getAuth());
        return ResultWrapper.getResponse(result);
    }

    @PostMapping("/worker/reject")
    public ResponseEntity<ApiResponse<Object>> reject(
            @RequestParam Long appId
    ) {
        var result = appointmentService.reject(appId, getAuth());
        return ResultWrapper.getResponse(result);
    }


}
