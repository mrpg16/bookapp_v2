package prod.bookapp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OnboardingCreateDTO {
    private List<WorkingHoursCreateDTO> workingHours;
    private List<ProposalCreateWVenueDTO> proposals;
}
