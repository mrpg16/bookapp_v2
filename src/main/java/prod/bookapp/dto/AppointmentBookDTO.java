package prod.bookapp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentBookDTO {
    private Long workerId;
    private Long proposalId;
    private Long venueId;
    private Long pricePackId;
    private LocalDate date;
    private LocalTime time;
}
