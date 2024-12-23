package prod.bookapp.dto.converter;

import org.springframework.stereotype.Component;
import prod.bookapp.dto.ProposalAppointmentViewDTO;
import prod.bookapp.dto.ProposalViewDTO;
import prod.bookapp.entity.Proposal;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProposalViewDTOConverter {
    private final VenueViewDTOConverter venueViewDTOConverter;

    public ProposalViewDTOConverter(VenueViewDTOConverter venueViewDTOConverter) {
        this.venueViewDTOConverter = venueViewDTOConverter;
    }

    public ProposalViewDTO convertToProposalViewDTO(Proposal proposal) {
        ProposalViewDTO proposalViewDTO = new ProposalViewDTO();
        proposalViewDTO.setId(proposal.getId());
        proposalViewDTO.setName(proposal.getName());
        proposalViewDTO.setDescription(proposal.getDescription());
        proposalViewDTO.setVenues(venueViewDTOConverter.convertToViewDTO(proposal.getVenues()));
        proposalViewDTO.setPricePacks(proposal.getPricePacks());
        proposalViewDTO.setOnline(proposal.isOnline());
        return proposalViewDTO;
    }

    public List<ProposalViewDTO> convertToProposalViewDTO(List<Proposal> proposals) {
        List<ProposalViewDTO> proposalViewDTOList = new ArrayList<>();
        for (Proposal proposal : proposals) {
            proposalViewDTOList.add(convertToProposalViewDTO(proposal));
        }
        return proposalViewDTOList;
    }

    public ProposalAppointmentViewDTO convertToProposalBookingViewDTO(Proposal proposal) {
        ProposalAppointmentViewDTO proposalAppointmentViewDTO = new ProposalAppointmentViewDTO();
        proposalAppointmentViewDTO.setId(proposal.getId());
        proposalAppointmentViewDTO.setName(proposal.getName());
        proposalAppointmentViewDTO.setDescription(proposal.getDescription());
        proposalAppointmentViewDTO.setOnline(proposal.isOnline());
        return proposalAppointmentViewDTO;
    }
}
