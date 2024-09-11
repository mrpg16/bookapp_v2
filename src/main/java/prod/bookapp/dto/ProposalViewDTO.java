package prod.bookapp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProposalViewDTO {
    private Long id;
    private String name;
    private String description;
    private int duration;
    private VenueViewDTO venue;
    private boolean online;
}
