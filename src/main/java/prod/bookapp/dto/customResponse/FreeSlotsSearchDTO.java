package prod.bookapp.dto.customResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.web.PagedModel;
import prod.bookapp.dto.ProposalViewDTO;
import prod.bookapp.dto.UserViewDTO;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FreeSlotsSearchDTO {
    private PagedModel<?> timeSlots;
    private UserViewDTO worker;
    private ProposalViewDTO proposal;
}
