package prod.bookapp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProposalCreateDTO {
    private String name;
    private String description;
    private int duration;
    private long venueId;
    private boolean online;
}
