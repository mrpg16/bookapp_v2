package prod.bookapp.service;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import prod.bookapp.dto.AppointmentBookDTO;
import prod.bookapp.dto.TimeSlotDTO;
import prod.bookapp.entity.Appointment;
import prod.bookapp.entity.Proposal;
import prod.bookapp.entity.User;
import prod.bookapp.enums.Enums;

import java.time.LocalDate;
import java.time.LocalTime;

@Service
public class BookingService {
    private final AppointmentService appointmentService;
    private final TimeSlotService timeSlotService;
    private final ProposalService proposalService;
    private final UserService userService;

    public BookingService(AppointmentService appointmentService, TimeSlotService timeSlotService, ProposalService proposalService, UserService userService) {
        this.appointmentService = appointmentService;
        this.timeSlotService = timeSlotService;
        this.proposalService = proposalService;
        this.userService = userService;
    }

    private User getAuthUser(Authentication authentication) {
        return (User) authentication.getPrincipal();
    }

    @Transactional
    public String book(AppointmentBookDTO bookDTO, Authentication auth) {
        LocalDate date = bookDTO.getDate();
        LocalTime timeStart = bookDTO.getTime();
        Long workerId = bookDTO.getWorkerId();
        Long proposalId = bookDTO.getProposalId();
        User client = getAuthUser(auth);
        if (appointmentService.hasAppointment(date, timeStart, client)) {
            return "You already have an appointment on that date and time";
        }
        TimeSlotDTO freeSlot = timeSlotService.getFreeSlotByDateTimeAndProposalIdAndWorkerId(date, timeStart, proposalId, workerId);
        if (freeSlot == null) {
            return "There is no free slot";
        }
        Proposal proposal = proposalService.getProposalById(proposalId);
        Appointment appointment = new Appointment();
        appointment.setClient(client);
        appointment.setDate(date);
        appointment.setTimeStart(timeStart);
        appointment.setTimeEnd(timeStart.plusMinutes(proposal.getDurationMin()));
        appointment.setProposal(proposal);
        appointment.setWorker(userService.getUserById(workerId));
        appointment.setDurationMin(proposal.getDurationMin());
        appointment.setStatus(Enums.APPOINTMENT_STATUS_UNCONFIRMED.getValue());
        appointmentService.save(appointment);
        return appointment.getId().toString();
    }
}
