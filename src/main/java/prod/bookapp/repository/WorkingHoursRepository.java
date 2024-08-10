package prod.bookapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import prod.bookapp.entity.User;
import prod.bookapp.entity.WorkingHours;

import java.util.List;

@Repository
public interface WorkingHoursRepository extends JpaRepository<WorkingHours, Long> {
    List<WorkingHours> findByOwner(User ownerId);

    void deleteAllByOwner(User owner);

    WorkingHours findByOwnerAndDayOfWeek(User owner, int day);
}
