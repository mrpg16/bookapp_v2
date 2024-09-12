package prod.bookapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import prod.bookapp.entity.Proposal;
import prod.bookapp.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProposalRepository extends JpaRepository<Proposal, Long> {
    Optional<Proposal> findByIdAndOwnerAndDeletedFalse(Long id, User owner);

    List<Proposal> findAllByOwnerAndDeletedFalse(User owner);


}
