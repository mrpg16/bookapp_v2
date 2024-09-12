package prod.bookapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import prod.bookapp.entity.User;
import prod.bookapp.entity.WorkingHours;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkingHoursRepository extends JpaRepository<WorkingHours, Long> {
    List<WorkingHours> findAllByOwner(User owner);

    void deleteAllByOwner(User owner);

    Optional<WorkingHours> findByOwnerAndDayOfWeek(User owner, int day);
}
