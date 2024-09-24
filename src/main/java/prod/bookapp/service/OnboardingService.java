package prod.bookapp.service;

import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import prod.bookapp.dto.OnboardingCreateDTO;
import prod.bookapp.dto.ProposalCreateWVenueDTO;
import prod.bookapp.dto.VenueCreateDTO;
import prod.bookapp.dto.WorkingHoursCreateDTO;
import prod.bookapp.entity.Proposal;
import prod.bookapp.entity.User;
import prod.bookapp.entity.WorkingHours;

import java.util.ArrayList;
import java.util.List;

@Service
public class OnboardingService {

    private final ProposalService proposalService;
    private final VenueService venueService;
    private final WorkingHoursService workingHoursService;

    public OnboardingService(ProposalService proposalService, VenueService venueService, WorkingHoursService workingHoursService) {
        this.proposalService = proposalService;
        this.venueService = venueService;
        this.workingHoursService = workingHoursService;
    }

    private User getAuthUser(Authentication authentication) {
        return (User) authentication.getPrincipal();
    }

    @Transactional
    public String create(OnboardingCreateDTO onboardingCreateDTO, Authentication authentication) {
        List<ProposalCreateWVenueDTO> proposalCreateWVenueDTOs = onboardingCreateDTO.getProposals();
        List<WorkingHoursCreateDTO> whDTOList = onboardingCreateDTO.getWorkingHours();
        User owner = getAuthUser(authentication);
        List<Proposal> propsToSave = new ArrayList<>();
        for (ProposalCreateWVenueDTO proposalCreateWVenueDTO : proposalCreateWVenueDTOs) {
            List<VenueCreateDTO> propVenues = proposalCreateWVenueDTO.getVenues();
            var validationResult = proposalService.validateProposalWithVenueDTO(proposalCreateWVenueDTO, propVenues);
            if (validationResult != null) {
                return validationResult;
            }
        }
        for (ProposalCreateWVenueDTO proposalCreateWVenueDTO : proposalCreateWVenueDTOs) {
            Proposal proposal = new Proposal();
            proposal.setOwner(owner);
            proposal.setName(proposalCreateWVenueDTO.getName());
            proposal.setDescription(proposalCreateWVenueDTO.getDescription());
            proposal.setDurationMin(proposalCreateWVenueDTO.getDuration());
            proposal.setOnline(proposalCreateWVenueDTO.isOnline());
            proposal.setVenues(venueService.createWithoutValidation(proposalCreateWVenueDTO.getVenues(), authentication));

            propsToSave.add(proposal);
        }
        var validationResult = workingHoursService.validateWH(whDTOList);
        if (validationResult != null) {
            return validationResult;
        }
        List<WorkingHours> whList = new ArrayList<>();
        for (WorkingHoursCreateDTO whDTO : whDTOList) {
            WorkingHours workingHours = new WorkingHours();
            workingHours.setOwner(owner);
            workingHours.setDayOfWeek(whDTO.getDayOfWeek());
            workingHours.setStartTime(whDTO.getStartTime());
            workingHours.setEndTime(whDTO.getEndTime());
            whList.add(workingHours);
        }
        List<WorkingHours> existedWH = workingHoursService.getAllByOwner(owner);
        if (!existedWH.isEmpty()) {
            workingHoursService.deleteAllByOwner(owner);
        }

        proposalService.saveAll(propsToSave);
        workingHoursService.saveAll(whList);
        return "Proposal created successfully: "
                + propsToSave.stream().map(l -> l.getId().toString()).toList()
                + " | "
                + "Working time created successfully: "
                + whList.stream().map(l -> l.getId().toString()).toList()
                ;
    }

}
