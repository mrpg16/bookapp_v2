package prod.bookapp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import prod.bookapp.entity.PricePack;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProposalViewDTO {
    private Long id;
    private String name;
    private String description;
    private List<VenueViewDTO> venues;
    private boolean online;
    private List<PricePack> pricePacks;
}
