package prod.bookapp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.web.PagedModel;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProposalCustomerViewDTO {
    private PagedModel<?> proposals;
    private UserViewDTO worker;
}
