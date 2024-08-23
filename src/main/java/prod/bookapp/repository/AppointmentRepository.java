package prod.bookapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import prod.bookapp.entity.Appointment;
import prod.bookapp.entity.User;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findAllByWorker(User worker);

    List<Appointment> findAllByWorkerAndDate(User worker, LocalDate date);

    List<Appointment> findAllByWorkerAndDateAndStatusIsNot(User worker, LocalDate date, String status);

    List<Appointment> findAllByClientAndDate(User client, LocalDate date);

    List<Appointment> findAllByWorkerAndDateBetween(User worker, LocalDate start, LocalDate end);

    List<Appointment> findAllByClientAndDateBetween(User client, LocalDate start, LocalDate end);

    Appointment findByIdAndWorkerAndStatus(long id, User worker, String status);
}
