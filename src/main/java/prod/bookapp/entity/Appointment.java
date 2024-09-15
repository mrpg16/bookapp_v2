package prod.bookapp.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "app_appointment")
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne()
    @JoinColumn(name = "worker_id")
    private User worker;
    @ManyToOne()
    @JoinColumn(name = "client_id")
    private User client;
    @ManyToOne()
    @JoinColumn(name = "proposal_id")
    private Proposal proposal;
    @ManyToOne()
    @JoinColumn(name = "venue_id")
    private Venue venue;
    private LocalDate date;
    private LocalTime timeStart;
    private LocalTime timeEnd;
    private int durationMin;
    private String status;
}
