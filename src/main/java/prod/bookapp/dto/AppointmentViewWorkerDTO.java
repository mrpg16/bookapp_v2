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
public class AppointmentViewWorkerDTO {
    private Long id;
    private ProposalViewDTO proposal;
    private UserViewDTO client;
    private String status;
    private LocalDate date;
    private LocalTime timeStart;
    private LocalTime timeEnd;
    private int durationMin;
}
