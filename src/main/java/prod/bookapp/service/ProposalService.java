package prod.bookapp.service;

import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import prod.bookapp.dto.ProposalCreateDTO;
import prod.bookapp.entity.Proposal;
import prod.bookapp.entity.User;
import prod.bookapp.repository.ProposalRepository;

@Service
public class ProposalService {

    private final ProposalRepository proposalRepository;


    public ProposalService(ProposalRepository proposalRepository) {
        this.proposalRepository = proposalRepository;
    }

    private User getAuthUser(Authentication authentication) {
        return (User) authentication.getPrincipal();
    }

    @Transactional
    public String create(ProposalCreateDTO proposalCreateDTO, Authentication authentication) {
        Proposal proposal = new Proposal();
        proposal.setName(proposalCreateDTO.getName());
        proposal.setDescription(proposalCreateDTO.getDescription());
        proposal.setDurationMin(proposalCreateDTO.getDuration());
        proposal.setOwner(getAuthUser(authentication));
        proposalRepository.save(proposal);
        return proposal.getId().toString();
    }

    public Proposal getProposalByIdAndOwner(Long proposalId, User owner) {
        return proposalRepository.findByIdAndOwner(proposalId, owner).orElse(null);
    }

    public Proposal getProposalById(Long proposalId) {
        return proposalRepository.findById(proposalId).orElse(null);
    }
}
