package prod.bookapp.dto.interfaces;

import prod.bookapp.dto.PricePackCreateDTO;

import java.util.List;

public interface ProposalDTO {
    String getName();

    boolean isOnline();

    List<PricePackCreateDTO> getPricePacks();

}
