package prod.bookapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import prod.bookapp.entity.Proposal;
import prod.bookapp.entity.User;
import prod.bookapp.entity.Venue;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProposalRepository extends JpaRepository<Proposal, Long> {
    @Query("SELECT DISTINCT p FROM Proposal p JOIN FETCH p.venues v WHERE p.id = :id AND p.owner = :owner AND p.deleted = false AND v.deleted = false")
    Optional<Proposal> findByIdAndOwnerAndDeletedFalse(@Param("id") Long id, @Param("owner") User owner);

    @Query("SELECT p FROM Proposal p JOIN FETCH p.venues v WHERE p.id = :id AND p.deleted = false AND v.deleted = false")
    Optional<Proposal> findByIdAndDeletedFalse(@Param("id") Long id);

    @Query("SELECT p FROM Proposal p JOIN FETCH p.venues v WHERE p.owner = :owner AND p.deleted = false AND v.deleted = false")
    List<Proposal> findAllByOwnerAndDeletedFalse(@Param("owner") User owner);

    @Query("SELECT p FROM Proposal p JOIN FETCH p.venues v WHERE v = :venue AND p.deleted = false and v.deleted = false")
    List<Proposal> findAllByVenueAndDeletedFalse(Venue venue);


}
