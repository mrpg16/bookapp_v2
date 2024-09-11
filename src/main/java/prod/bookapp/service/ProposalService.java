package prod.bookapp.service;

import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import prod.bookapp.dto.ProposalCreateDTO;
import prod.bookapp.dto.ProposalCreateWVenueDTO;
import prod.bookapp.dto.VenueCreateDTO;
import prod.bookapp.entity.Proposal;
import prod.bookapp.entity.User;
import prod.bookapp.entity.Venue;
import prod.bookapp.repository.ProposalRepository;

@Service
public class ProposalService {

    private final ProposalRepository proposalRepository;
    private final VenueService venueService;


    public ProposalService(ProposalRepository proposalRepository, VenueService venueService) {
        this.proposalRepository = proposalRepository;
        this.venueService = venueService;
    }

    private User getAuthUser(Authentication authentication) {
        return (User) authentication.getPrincipal();
    }


    private String validateProposal(ProposalCreateDTO proposalCreateDTO, Venue venue) {
        if (proposalCreateDTO.getName() == null || proposalCreateDTO.getName().isEmpty()) {
            return "Name cannot be empty";
        }
        if (proposalCreateDTO.getDuration() <= 0) {
            return "Duration cannot be <= 0";
        }
        if (proposalCreateDTO.isOnline() != venue.isOnline()) {
            return "Venue type mismatch";
        }
        return null;
    }

    private String validateProposalWithVenue(ProposalCreateWVenueDTO proposalCreateWVenueDTO, VenueCreateDTO venueCreateDTO) {
        if(proposalCreateWVenueDTO.isOnline() != venueCreateDTO.isOnline()) {
            return "Venue type mismatch";
        }
        if (proposalCreateWVenueDTO.getName() == null || proposalCreateWVenueDTO.getName().isEmpty()) {
            return "Name cannot be empty";
        }
        if (proposalCreateWVenueDTO.getDuration() <= 0) {
            return "Duration cannot be <= 0";
        }
        return venueService.validateVenue(venueCreateDTO);
    }

    @Transactional
    public String create(ProposalCreateDTO proposalCreateDTO, Authentication authentication) {
        var venueId = proposalCreateDTO.getVenueId();
        var owner = getAuthUser(authentication);
        var venue = venueService.findByIdAndOwnerAndDeletedFalse(venueId, owner);
        if (venue == null) {
            return "Venue not found";
        }
        var validationResult = validateProposal(proposalCreateDTO, venue);
        if (validationResult != null) {
            return validationResult;
        }
        Proposal proposal = new Proposal();
        proposal.setOwner(getAuthUser(authentication));
        proposal.setName(proposalCreateDTO.getName());
        proposal.setDescription(proposalCreateDTO.getDescription());
        proposal.setDurationMin(proposalCreateDTO.getDuration());
        proposal.setOnline(proposalCreateDTO.isOnline());
        proposal.setVenue(venue);
        proposalRepository.save(proposal);
        return proposal.getId().toString();
    }

    @Transactional
    public String createWithVenue(ProposalCreateWVenueDTO proposalCreateWVenueDTO, Authentication authentication) {
        VenueCreateDTO venueCreateDTO = proposalCreateWVenueDTO.getVenue();
        var validationResult = validateProposalWithVenue(proposalCreateWVenueDTO, venueCreateDTO);
        if(validationResult != null){
            return validationResult;
        }
        Proposal proposal = new Proposal();
        proposal.setOwner(getAuthUser(authentication));
        proposal.setName(proposalCreateWVenueDTO.getName());
        proposal.setDescription(proposalCreateWVenueDTO.getDescription());
        proposal.setDurationMin(proposalCreateWVenueDTO.getDuration());
        proposal.setOnline(proposalCreateWVenueDTO.isOnline());
        proposal.setVenue(venueService.createWithoutValidation(venueCreateDTO, authentication));
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
