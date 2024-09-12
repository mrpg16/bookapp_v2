package prod.bookapp.service;

import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import prod.bookapp.dto.ProposalCreateDTO;
import prod.bookapp.dto.ProposalCreateWVenueDTO;
import prod.bookapp.dto.ProposalViewDTO;
import prod.bookapp.dto.VenueCreateDTO;
import prod.bookapp.dto.converter.VenueViewDTOConverter;
import prod.bookapp.entity.Proposal;
import prod.bookapp.entity.User;
import prod.bookapp.entity.Venue;
import prod.bookapp.repository.ProposalRepository;

@Service
public class ProposalService {

    private final ProposalRepository proposalRepository;
    private final VenueService venueService;
    private final VenueViewDTOConverter venueViewDTOConverter;


    public ProposalService(ProposalRepository proposalRepository, VenueService venueService, VenueViewDTOConverter venueViewDTOConverter) {
        this.proposalRepository = proposalRepository;
        this.venueService = venueService;
        this.venueViewDTOConverter = venueViewDTOConverter;
    }

    private User getAuthUser(Authentication authentication) {
        return (User) authentication.getPrincipal();
    }


    private String validateProposalDTO(ProposalCreateDTO proposalCreateDTO, Venue venue) {
        if (proposalCreateDTO.getName() == null || proposalCreateDTO.getName().isEmpty()) {
            return "Error: Name cannot be empty";
        }
        if (proposalCreateDTO.getDuration() <= 0) {
            return "Error: Duration cannot be <= 0";
        }
        if (proposalCreateDTO.isOnline() != venue.isOnline()) {
            return "Error: Venue type mismatch";
        }
        return null;
    }

    private String validateProposalWithVenueDTO(ProposalCreateWVenueDTO proposalCreateWVenueDTO, VenueCreateDTO venueCreateDTO) {
        if(proposalCreateWVenueDTO.isOnline() != venueCreateDTO.isOnline()) {
            return "Error: Venue type mismatch";
        }
        if (proposalCreateWVenueDTO.getName() == null || proposalCreateWVenueDTO.getName().isEmpty()) {
            return "Error: Name cannot be empty";
        }
        if (proposalCreateWVenueDTO.getDuration() <= 0) {
            return "Error: Duration cannot be <= 0";
        }
        return venueService.validateVenue(venueCreateDTO);
    }

    private String validateProposalWithVenueDTO(ProposalViewDTO proposalViewDTO) {
        if(proposalViewDTO.isOnline() != proposalViewDTO.getVenue().isOnline()) {
            return "Error: Venue type mismatch";
        }
        if (proposalViewDTO.getName() == null || proposalViewDTO.getName().isEmpty()) {
            return "Error: Name cannot be empty";
        }
        if (proposalViewDTO.getDuration() <= 0) {
            return "Error: Duration cannot be <= 0";
        }
        return venueService.validateVenue(proposalViewDTO.getVenue());
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

//    @Transactional
//    public String update(ProposalViewDTO proposalViewDTO, Authentication authentication){
//        User owner = getAuthUser(authentication);
//        Proposal proposal = getProposalByIdAndOwner(proposalViewDTO.getId(), owner);
//        if(proposal == null){
//            return "Proposal not found";
//        }
//        var validationResult = validateProposalWithVenueDTO(proposalViewDTO);
//        if(validationResult != null){
//            return validationResult;
//        }
//        Proposal proposalUpdate = new Proposal();
//        proposalUpdate.setOwner(owner);
//        proposalUpdate.setName(proposalViewDTO.getName());
//        proposalUpdate.setDescription(proposalViewDTO.getDescription());
//        proposalUpdate.setDurationMin(proposalViewDTO.getDuration());
//        proposalUpdate.setOnline(proposalViewDTO.isOnline());
//
//        proposalUpdate.setVenue(proposalViewDTO.getVenue());
//
//        return null;
//    }


    public Proposal getProposalByIdAndOwner(Long proposalId, User owner) {
        return proposalRepository.findByIdAndOwnerAndDeletedFalse(proposalId, owner).orElse(null);
    }

    public Proposal getProposalById(Long proposalId) {
        return proposalRepository.findById(proposalId).orElse(null);
    }
}
