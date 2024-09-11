package prod.bookapp.dto.converter;

import org.springframework.stereotype.Component;
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
        proposalViewDTO.setVenue(venueViewDTOConverter.convertToViewDTO(proposal.getVenue()));
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
}
