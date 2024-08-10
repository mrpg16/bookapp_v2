package prod.bookapp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentCreateDTO {
    private Long workerId;
    private Long proposalId;
    private LocalDateTime dateTimeStart;
    private LocalDateTime dateTimeEnd;
}
