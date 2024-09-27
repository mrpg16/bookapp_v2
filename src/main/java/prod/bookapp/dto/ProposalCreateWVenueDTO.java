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
public class ProposalCreateWVenueDTO implements ProposalDTO {
    private String name;
    private String description;
    private List<VenueCreateDTO> venues;
    private boolean online;
    private List<PricePackCreateDTO> pricePacks;
}
