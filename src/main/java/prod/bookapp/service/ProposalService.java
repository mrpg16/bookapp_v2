package prod.bookapp.service;

import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import prod.bookapp.dto.*;
import prod.bookapp.dto.converter.ProposalViewDTOConverter;
import prod.bookapp.dto.converter.VenueViewDTOConverter;
import prod.bookapp.dto.interfaces.ProposalDTO;
import prod.bookapp.dto.interfaces.VenueDTO;
import prod.bookapp.entity.Proposal;
import prod.bookapp.entity.User;
import prod.bookapp.entity.Venue;
import prod.bookapp.repository.ProposalRepository;
import prod.bookapp.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProposalService {

    private final ProposalRepository proposalRepository;
    private final VenueService venueService;
    private final VenueViewDTOConverter venueViewDTOConverter;
    private final ProposalViewDTOConverter proposalViewDTOConverter;
    private final UserRepository userRepository;


    public ProposalService(ProposalRepository proposalRepository, VenueService venueService, VenueViewDTOConverter venueViewDTOConverter, ProposalViewDTOConverter proposalViewDTOConverter, UserRepository userRepository) {
        this.proposalRepository = proposalRepository;
        this.venueService = venueService;
        this.venueViewDTOConverter = venueViewDTOConverter;
        this.proposalViewDTOConverter = proposalViewDTOConverter;
        this.userRepository = userRepository;
    }

    private User getAuthUser(Authentication authentication) {
        return (User) authentication.getPrincipal();
    }


    private String validateProposalDTO(ProposalDTO proposalDTO, List<Venue> venue) {
        if (proposalDTO.getName() == null || proposalDTO.getName().isEmpty()) {
            return "Error: Name cannot be empty";
        }
        if (proposalDTO.getDuration() <= 0) {
            return "Error: Duration cannot be <= 0";
        }
        for (Venue v : venue) {
            if (proposalDTO.isOnline() != v.isOnline()) {
                return "Error: Venue type mismatch";
            }
        }
        return null;
    }

    private String validateProposalWithVenueDTO(ProposalDTO proposalDTO, List<VenueCreateDTO> venueDTO) {
        for (VenueDTO v : venueDTO) {
            if (proposalDTO.isOnline() != v.isOnline()) {
                return "Error: Venue type mismatch";
            }
            var venueValidation = venueService.validateVenue(v);
            if (venueValidation != null) {
                return venueValidation;
            }
        }
        if (proposalDTO.getName() == null || proposalDTO.getName().isEmpty()) {
            return "Error: Name cannot be empty";
        }
        if (proposalDTO.getDuration() <= 0) {
            return "Error: Duration cannot be <= 0";
        }
        return null;
    }

    @Transactional
    public String create(ProposalCreateDTO proposalCreateDTO, Authentication authentication) {
        var venueIds = proposalCreateDTO.getVenueIds();
        var owner = getAuthUser(authentication);
        List<Venue> propVenues = new ArrayList<>();
        for (var venueId : venueIds) {
            var venue = venueService.findByIdAndOwnerAndDeletedFalse(venueId, owner);
            if (venue == null) {
                return "Error: Venue not found";
            }
            propVenues.add(venue);
        }
        var validationResult = validateProposalDTO(proposalCreateDTO, propVenues);
        if (validationResult != null) {
            return validationResult;
        }
        Proposal proposal = new Proposal();
        proposal.setOwner(getAuthUser(authentication));
        proposal.setName(proposalCreateDTO.getName());
        proposal.setDescription(proposalCreateDTO.getDescription());
        proposal.setDurationMin(proposalCreateDTO.getDuration());
        proposal.setOnline(proposalCreateDTO.isOnline());
        proposal.setVenues(propVenues);
        proposalRepository.save(proposal);
        return proposal.getId().toString();
    }

    @Transactional
    public String createWithVenue(ProposalCreateWVenueDTO proposalCreateWVenueDTO, Authentication authentication) {
        List<VenueCreateDTO> propVenues = proposalCreateWVenueDTO.getVenues();
        var validationResult = validateProposalWithVenueDTO(proposalCreateWVenueDTO, propVenues);
        if (validationResult != null) {
            return validationResult;
        }
        Proposal proposal = new Proposal();
        proposal.setOwner(getAuthUser(authentication));
        proposal.setName(proposalCreateWVenueDTO.getName());
        proposal.setDescription(proposalCreateWVenueDTO.getDescription());
        proposal.setDurationMin(proposalCreateWVenueDTO.getDuration());
        proposal.setOnline(proposalCreateWVenueDTO.isOnline());
        proposal.setVenues(venueService.createWithoutValidation(propVenues, authentication));
        proposalRepository.save(proposal);
        return proposal.getId().toString();
    }

    @Transactional
    public String update(ProposalUpdateDTO proposalUpdateDTO, Authentication authentication) {
        var venueIds = proposalUpdateDTO.getVenueIds();
        User owner = getAuthUser(authentication);
        Proposal proposal = getProposalByIdAndOwner(proposalUpdateDTO.getId(), owner);
        if (proposal == null) {
            return "Error: Proposal not found";
        }
        List<Venue> propVenues = new ArrayList<>();
        for (var venueId : venueIds) {
            var venue = venueService.findByIdAndOwnerAndDeletedFalse(venueId, owner);
            if (venue == null) {
                return "Error: Venue not found";
            }
            propVenues.add(venue);
        }
        var validationResult = validateProposalDTO(proposalUpdateDTO, propVenues);
        if (validationResult != null) {
            return validationResult;
        }
        proposal.setOwner(owner);
        proposal.setName(proposalUpdateDTO.getName());
        proposal.setDescription(proposalUpdateDTO.getDescription());
        proposal.setDurationMin(proposalUpdateDTO.getDuration());
        proposal.setOnline(proposalUpdateDTO.isOnline());
        proposal.setVenues(propVenues);
        proposalRepository.save(proposal);
        return proposal.getId().toString();
    }

    @Transactional
    public String delete(long id, Authentication authentication) {
        User owner = getAuthUser(authentication);
        Proposal proposal = proposalRepository.findByIdAndOwnerAndDeletedFalse(id, owner).orElse(null);
        if (proposal == null) {
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
        if (proposal == null) {
            return null;
        }
        return proposalViewDTOConverter.convertToProposalViewDTO(proposal);
    }

    public List<ProposalViewDTO> getAll(Authentication authentication) {
        User owner = getAuthUser(authentication);
        List<Proposal> props = proposalRepository.findAllByOwnerAndDeletedFalse(owner);
        return proposalViewDTOConverter.convertToProposalViewDTO(props);
    }

    public List<ProposalViewDTO> getAllByWorkerId(long id) {
        User owner = userRepository.findById(id).orElse(null);
        List<Proposal> props = proposalRepository.findAllByOwnerAndDeletedFalse(owner);
        return proposalViewDTOConverter.convertToProposalViewDTO(props);
    }

    public List<VenueViewDTO> getAllVenuesOfProposalByIdAndWorkerId(long workerId, long proposalId) {
        User owner = userRepository.findById(workerId).orElse(null);
        Proposal prop = proposalRepository.findByIdAndOwnerAndDeletedFalse(proposalId, owner).orElse(null);
        if (prop == null) {
            return null;
        }
        List<Venue> venues = prop.getVenues();
        return venueViewDTOConverter.convertToViewDTO(venues);
    }


}
