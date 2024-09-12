package prod.bookapp.service;

import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import prod.bookapp.dto.VenueCreateDTO;
import prod.bookapp.dto.VenueViewDTO;
import prod.bookapp.dto.converter.VenueViewDTOConverter;
import prod.bookapp.dto.interfaces.VenueDTO;
import prod.bookapp.entity.User;
import prod.bookapp.entity.Venue;
import prod.bookapp.repository.VenueRepository;

import java.util.List;

@Service
public class VenueService {
    private final VenueRepository venueRepository;
    private final VenueViewDTOConverter venueViewDTOConverter;

    public VenueService(VenueRepository venueRepository, VenueViewDTOConverter venueViewDTOConverter) {
        this.venueRepository = venueRepository;
        this.venueViewDTOConverter = venueViewDTOConverter;
    }

    private User getAuthUser(Authentication authentication) {
        return (User) authentication.getPrincipal();
    }

    public String validateVenue(VenueDTO venueDTO) {
        if (venueDTO.getName() == null || venueDTO.getName().isEmpty()) {
            return "Error: Venue name cannot be empty";
        }

        boolean online = venueDTO.isOnline();
        if (online) {
            if (venueDTO.getOnlineProvider() == null || venueDTO.getOnlineProvider().isEmpty() ||
                    venueDTO.getLink() == null || venueDTO.getLink().isEmpty()) {
                return "Error: Link or provider is empty for the online venue";
            }
        } else {
            if ((venueDTO.getPhone() == null || venueDTO.getPhone().isEmpty()) &&
                    (venueDTO.getFullAddress() == null || venueDTO.getFullAddress().isEmpty())) {
                return "Error: Phone and address is empty for the offline venue";
            }
        }
        return null;
    }

    @Transactional
    public String create(VenueCreateDTO venueDTO, Authentication authentication) {
        var validationResult = validateVenue(venueDTO);
        if (validationResult != null) {
            return validationResult;
        }
        Venue venue = new Venue();
        venue.setName(venueDTO.getName());
        venue.setFullAddress(venueDTO.getFullAddress());
        venue.setPhone(venueDTO.getPhone());
        venue.setOwner(getAuthUser(authentication));
        venue.setOnline(venueDTO.isOnline());
        venue.setOnlineProvider(venueDTO.getOnlineProvider());
        venue.setLink(venueDTO.getLink());
        venueRepository.save(venue);
        return venue.getId().toString();
    }

    public Venue createWithoutValidation(VenueCreateDTO venueDTO, Authentication authentication) {
        Venue venue = new Venue();
        venue.setName(venueDTO.getName());
        venue.setFullAddress(venueDTO.getFullAddress());
        venue.setPhone(venueDTO.getPhone());
        venue.setOwner(getAuthUser(authentication));
        venue.setOnline(venueDTO.isOnline());
        venue.setOnlineProvider(venueDTO.getOnlineProvider());
        venue.setLink(venueDTO.getLink());
        venueRepository.save(venue);
        return venue;
    }

    @Transactional
    public String update(VenueViewDTO venueDTO, Authentication authentication) {
        User owner = getAuthUser(authentication);
        Venue venue = venueRepository.findByIdAndOwnerAndDeletedFalse(venueDTO.getId(), owner).orElse(null);
        if (venue == null) {
            return "Error: Venue not found";
        }
        var validationResult = validateVenue(venueDTO);
        if (validationResult != null) {
            return validationResult;
        }
        venue.setName(venueDTO.getName());
        venue.setFullAddress(venueDTO.getFullAddress());
        venue.setPhone(venueDTO.getPhone());
        venue.setOnline(venueDTO.isOnline());
        venue.setOnlineProvider(venueDTO.getOnlineProvider());
        venue.setLink(venueDTO.getLink());
        venueRepository.save(venue);
        return venue.getId().toString();
    }

    @Transactional
    public String delete(long venueId, Authentication authentication) {
        User owner = getAuthUser(authentication);
        Venue venue = venueRepository.findByIdAndOwnerAndDeletedFalse(venueId, owner).orElse(null);
        if (venue == null) {
            return "Error: Venue not found";
        }
        venue.setDeleted(true);
        venueRepository.save(venue);
        return venue.getId().toString();
    }

    public List<VenueViewDTO> getAllVenues(Authentication authentication) {
        User owner = getAuthUser(authentication);
        List<Venue> venues = venueRepository.findAllByOwnerAndDeletedFalse(owner);
        return venueViewDTOConverter.convertToViewDTO(venues);
    }

    public Venue findByIdAndOwnerAndDeletedFalse(long venueId, User owner) {
        return venueRepository.findByIdAndOwnerAndDeletedFalse(venueId, owner).orElse(null);
    }

    public VenueViewDTO getById(long venueId, Authentication authentication) {
        User owner = getAuthUser(authentication);
        Venue venue = findByIdAndOwnerAndDeletedFalse(venueId, owner);
        if(venue == null){
            return null;
        }
        return venueViewDTOConverter.convertToViewDTO(venue);
    }

    public List<VenueViewDTO> getAllOnlineVenues(Authentication authentication) {
        User owner = getAuthUser(authentication);
        List<Venue> venues = venueRepository.findAllByOwnerAndDeletedFalseAndOnline(owner, true);
        return venueViewDTOConverter.convertToViewDTO(venues);
    }

    public List<VenueViewDTO> getAllOfflineVenues(Authentication authentication) {
        User owner = getAuthUser(authentication);
        List<Venue> venues = venueRepository.findAllByOwnerAndDeletedFalseAndOnline(owner, false);
        return venueViewDTOConverter.convertToViewDTO(venues);
    }
}
