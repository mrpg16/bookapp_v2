package prod.bookapp.service;

import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import prod.bookapp.dto.*;
import prod.bookapp.dto.converter.ProposalViewDTOConverter;
import prod.bookapp.dto.interfaces.ProposalDTO;
import prod.bookapp.dto.interfaces.VenueDTO;
import prod.bookapp.entity.Proposal;
import prod.bookapp.entity.User;
import prod.bookapp.entity.Venue;
import prod.bookapp.repository.ProposalRepository;

import java.util.List;

@Service
public class ProposalService {

    private final ProposalRepository proposalRepository;
    private final VenueService venueService;
    private final ProposalViewDTOConverter proposalViewDTOConverter;


    public ProposalService(ProposalRepository proposalRepository, VenueService venueService, ProposalViewDTOConverter proposalViewDTOConverter) {
        this.proposalRepository = proposalRepository;
        this.venueService = venueService;
        this.proposalViewDTOConverter = proposalViewDTOConverter;
    }

    private User getAuthUser(Authentication authentication) {
        return (User) authentication.getPrincipal();
    }


    private String validateProposalDTO(ProposalDTO proposalDTO, Venue venue) {
        if (proposalDTO.getName() == null || proposalDTO.getName().isEmpty()) {
            return "Error: Name cannot be empty";
        }
        if (proposalDTO.getDuration() <= 0) {
            return "Error: Duration cannot be <= 0";
        }
        if (proposalDTO.isOnline() != venue.isOnline()) {
            return "Error: Venue type mismatch";
        }
        return null;
    }

    private String validateProposalWithVenueDTO(ProposalDTO proposalDTO, VenueDTO venueDTO) {
        if (proposalDTO.isOnline() != venueDTO.isOnline()) {
            return "Error: Venue type mismatch";
        }
        if (proposalDTO.getName() == null || proposalDTO.getName().isEmpty()) {
            return "Error: Name cannot be empty";
        }
        if (proposalDTO.getDuration() <= 0) {
            return "Error: Duration cannot be <= 0";
        }
        return venueService.validateVenue(venueDTO);
    }

    @Transactional
    public String create(ProposalCreateDTO proposalCreateDTO, Authentication authentication) {
        var venueId = proposalCreateDTO.getVenueId();
        var owner = getAuthUser(authentication);
        var venue = venueService.findByIdAndOwnerAndDeletedFalse(venueId, owner);
        if (venue == null) {
            return "Error: Venue not found";
        }
        var validationResult = validateProposalDTO(proposalCreateDTO, venue);
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
        var validationResult = validateProposalWithVenueDTO(proposalCreateWVenueDTO, venueCreateDTO);
        if (validationResult != null) {
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

    @Transactional
    public String update(ProposalUpdateDTO proposalUpdateDTO, Authentication authentication) {
        User owner = getAuthUser(authentication);
        Proposal proposal = getProposalByIdAndOwner(proposalUpdateDTO.getId(), owner);
        if (proposal == null) {
            return "Error: Proposal not found";
        }
        Venue venue = venueService.findByIdAndOwnerAndDeletedFalse(proposalUpdateDTO.getVenueId(), owner);
        if (venue == null) {
            return "Error: Venue not found";
        }

        var validationResult = validateProposalDTO(proposalUpdateDTO, venue);
        if (validationResult != null) {
            return validationResult;
        }
        proposal.setOwner(owner);
        proposal.setName(proposalUpdateDTO.getName());
        proposal.setDescription(proposalUpdateDTO.getDescription());
        proposal.setDurationMin(proposalUpdateDTO.getDuration());
        proposal.setOnline(proposalUpdateDTO.isOnline());
        proposal.setVenue(venue);
        proposalRepository.save(proposal);
        return proposal.getId().toString();
    }

    @Transactional
    public String delete(long id, Authentication authentication){
        User owner = getAuthUser(authentication);
        Proposal proposal = proposalRepository.findByIdAndOwnerAndDeletedFalse(id, owner).orElse(null);
        if(proposal==null){
            return "Error: Proposal not found";
        }
        proposal.setDeleted(true);
        proposalRepository.save(proposal);
        return proposal.getId().toString();
    }


    public Proposal getProposalByIdAndOwner(Long proposalId, User owner) {
        return proposalRepository.findByIdAndOwnerAndDeletedFalse(proposalId, owner).orElse(null);
    }

    public Proposal getProposalById(Long proposalId) {
        return proposalRepository.findByIdAndDeletedFalse(proposalId).orElse(null);
    }

    public ProposalViewDTO getById(long id, Authentication authentication) {
        User owner = getAuthUser(authentication);
        Proposal proposal = proposalRepository.findByIdAndOwnerAndDeletedFalse(id, owner).orElse(null);
        if(proposal==null){
            return null;
        }
        return proposalViewDTOConverter.convertToProposalViewDTO(proposal);
    }

    public List<ProposalViewDTO> getAll(Authentication authentication) {
        User owner = getAuthUser(authentication);
        List<Proposal> props = proposalRepository.findAllByOwnerAndDeletedFalse(owner);
        return proposalViewDTOConverter.convertToProposalViewDTO(props);
    }
}
