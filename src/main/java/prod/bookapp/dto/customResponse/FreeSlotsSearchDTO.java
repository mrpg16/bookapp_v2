package prod.bookapp.dto.customResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.web.PagedModel;
import prod.bookapp.dto.VenueViewDTO;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FreeSlotsSearchDTO {
    private PagedModel<?> timeSlots;
    private Long workerId;
    private long proposalId;
    private List<VenueViewDTO> availableVenues;
}
