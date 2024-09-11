package prod.bookapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import prod.bookapp.entity.User;
import prod.bookapp.entity.Venue;

import java.util.List;

public interface VenueRepository  extends JpaRepository<Venue, Long> {
    Venue findByIdAndOwnerAndDeletedFalse(Long id, User owner);
    List<Venue> findAllByOwnerAndDeletedFalse(User owner);
}
