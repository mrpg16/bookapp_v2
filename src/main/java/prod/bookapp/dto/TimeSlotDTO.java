package prod.bookapp.dto;

import lombok.*;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDate;
import java.time.LocalTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Relation(collectionRelation = "timeSlots")
public class TimeSlotDTO {
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private String type;
    private AppointmentViewDTO appointment;
    private int durationMin;
}
