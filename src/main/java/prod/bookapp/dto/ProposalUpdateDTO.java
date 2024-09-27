package prod.bookapp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import prod.bookapp.dto.interfaces.ProposalDTO;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProposalUpdateDTO implements ProposalDTO {
    private long id;
    private String name;
    private String description;
    private long[] venueIds;
    private boolean online;
    private List<PricePackCreateDTO> pricePacks;
}
