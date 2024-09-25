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
        proposalViewDTO.setDuration(proposal.getDurationMin());
        proposalViewDTO.setVenues(venueViewDTOConverter.convertToViewDTO(proposal.getVenues()));
        proposalViewDTO.setOnline(proposal.isOnline());
        proposalViewDTO.setPrice(proposal.getPrice());
        proposalViewDTO.setCurrency(proposal.getCurrency());
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
        proposalAppointmentViewDTO.setDuration(proposal.getDurationMin());
        proposalAppointmentViewDTO.setOnline(proposal.isOnline());
        proposalAppointmentViewDTO.setPrice(proposal.getPrice());
        proposalAppointmentViewDTO.setCurrency(proposal.getCurrency());
        return proposalAppointmentViewDTO;
    }
}
