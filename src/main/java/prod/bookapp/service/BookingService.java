package prod.bookapp.service;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import prod.bookapp.dto.AppointmentBookDTO;
import prod.bookapp.dto.TimeSlotDTO;
import prod.bookapp.entity.Appointment;
import prod.bookapp.entity.Proposal;
import prod.bookapp.entity.User;
import prod.bookapp.entity.Venue;
import prod.bookapp.enums.Enums;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

@Service
public class BookingService {
    private final AppointmentService appointmentService;
    private final TimeSlotService timeSlotService;
    private final ProposalService proposalService;
    private final UserService userService;
    private final VenueService venueService;

    public BookingService(AppointmentService appointmentService, TimeSlotService timeSlotService, ProposalService proposalService, UserService userService, VenueService venueService) {
        this.appointmentService = appointmentService;
        this.timeSlotService = timeSlotService;
        this.proposalService = proposalService;
        this.userService = userService;
        this.venueService = venueService;
    }

    private User getAuthUser(Authentication authentication) {
        return (User) authentication.getPrincipal();
    }

    public String validateBooking(LocalDate date, LocalTime timeStart, User client, Long workerId, Long proposalId, Long venueId, Long pricePackId) {
        if (appointmentService.hasAppointment(date, timeStart, client)) {
            return "Error: You already have an appointment on that date and time";
        }
        TimeSlotDTO freeSlot = timeSlotService.getFreeSlotByDateTimeAndProposalIdAndWorkerId(date, timeStart, proposalId, workerId, pricePackId);
        if (freeSlot == null) {
            return "Error: There is no free slot";
        }
        Proposal proposal = proposalService.getProposalById(proposalId);
        var listOfVenue = proposal.getVenues();
        if (listOfVenue.stream().noneMatch(venue -> venue.getId().equals(venueId))) {
            return "Error: There is no venue";
        }
        return null;
    }

    @Transactional
    public String book(AppointmentBookDTO bookDTO, Authentication auth) {
        LocalDate date = bookDTO.getDate();
        LocalTime timeStart = bookDTO.getTime();
        Long workerId = bookDTO.getWorkerId();
        Long proposalId = bookDTO.getProposalId();
        Long venueId = bookDTO.getVenueId();
        Long pricePackId = bookDTO.getPricePackId();
        User client = getAuthUser(auth);
        var validationResult = validateBooking(date, timeStart, client, workerId, proposalId, venueId, pricePackId);
        if (validationResult != null) {
            return validationResult;
        }
        Proposal proposal = proposalService.getProposalById(proposalId);
        var pricePack = proposal.getPricePacks().stream().filter(p -> Objects.equals(p.getId(), pricePackId)).findFirst().orElse(null);
        Venue venue = venueService.getById(venueId);
        Appointment appointment = new Appointment();
        appointment.setWorker(userService.getUserById(workerId));
        appointment.setClient(client);
        appointment.setProposal(proposal);
        appointment.setVenue(venue);
        appointment.setDate(date);
        appointment.setTimeStart(timeStart);
        assert pricePack != null;
        appointment.setTimeEnd(timeStart.plusMinutes(pricePack.getDuration()));
        appointment.setDurationMin(pricePack.getDuration());
        appointment.setCurrency(pricePack.getCurrency());
        appointment.setPrice(pricePack.getPrice());
        appointment.setPricePackId(pricePack.getId());
        appointment.setStatus(Enums.APPOINTMENT_STATUS_UNCONFIRMED.getValue());

        appointmentService.save(appointment);
        return appointment.getId().toString();
    }
}
