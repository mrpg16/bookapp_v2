package prod.bookapp.dto.converter;

import org.springframework.stereotype.Component;
import prod.bookapp.dto.VenueViewDTO;
import prod.bookapp.entity.Venue;

import java.util.ArrayList;
import java.util.List;

@Component
public class VenueViewDTOConverter {
    public VenueViewDTO convertToViewDTO(Venue venue) {
        VenueViewDTO venueViewDTO = new VenueViewDTO();
        venueViewDTO.setId(venue.getId());
        venueViewDTO.setName(venue.getName());
        venueViewDTO.setFullAddress(venue.getFullAddress());
        venueViewDTO.setPhone(venue.getPhone());
        venueViewDTO.setOnline(venue.isOnline());
        venueViewDTO.setOnlineProvider(venue.getOnlineProvider());
        venueViewDTO.setLink(venue.getLink());
        return venueViewDTO;
    }

    public List<VenueViewDTO> convertToViewDTO(List<Venue> venues) {
        List<VenueViewDTO> venueViewDTOs = new ArrayList<>();
        for (Venue venue : venues) {
            venueViewDTOs.add(convertToViewDTO(venue));
        }
        return venueViewDTOs;
    }

}
