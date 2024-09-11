package prod.bookapp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import prod.bookapp.dto.interfaces.VenueDTO;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class VenueCreateDTO implements VenueDTO {
    private String name;
    private String fullAddress;
    private String phone;
    private boolean online;
    private String onlineProvider;
    private String link;
}
