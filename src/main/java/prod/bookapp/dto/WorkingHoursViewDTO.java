package prod.bookapp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class WorkingHoursViewDTO {
    private long id;
    private int dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;
}
