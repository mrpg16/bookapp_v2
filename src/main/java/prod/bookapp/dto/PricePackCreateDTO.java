package prod.bookapp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import prod.bookapp.dto.interfaces.PricePackDTO;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PricePackCreateDTO implements PricePackDTO {
    private Integer duration;
    private Double price;
    private String currency;
}
