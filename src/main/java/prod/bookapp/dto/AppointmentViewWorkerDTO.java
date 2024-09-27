package prod.bookapp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import prod.bookapp.entity.PricePack;

import java.time.LocalDate;
import java.time.LocalTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentViewWorkerDTO {
    private Long id;
    private ProposalAppointmentViewDTO proposal;
    private VenueViewDTO venue;
    private UserViewDTO client;
    private String status;
    private LocalDate date;
    private LocalTime timeStart;
    private LocalTime timeEnd;
    private int durationMin;
    private PricePack pricePack;
}
