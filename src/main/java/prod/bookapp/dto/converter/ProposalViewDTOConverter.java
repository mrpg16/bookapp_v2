package prod.bookapp.dto.converter;

import org.springframework.stereotype.Component;
import prod.bookapp.dto.ProposalViewDTO;
import prod.bookapp.entity.Proposal;

@Component
public class ProposalViewDTOConverter {
    public ProposalViewDTO convertToProposalViewDTO(Proposal proposal) {
        ProposalViewDTO proposalViewDTO = new ProposalViewDTO();
        proposalViewDTO.setId(proposal.getId());
        proposalViewDTO.setName(proposal.getName());
        proposalViewDTO.setDescription(proposal.getDescription());
        proposalViewDTO.setDurationMin(proposal.getDurationMin());
        return proposalViewDTO;
    }
}
