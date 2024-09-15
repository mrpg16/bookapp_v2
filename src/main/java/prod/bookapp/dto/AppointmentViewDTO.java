package prod.bookapp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentViewDTO {
    private Long id;
    private ProposalAppointmentViewDTO proposal;
    private VenueViewDTO venue;
    private UserViewDTO client;
    private String status;
}



