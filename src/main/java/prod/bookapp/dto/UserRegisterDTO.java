package prod.bookapp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterDTO {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String companyName;
    private String phoneNumber;
    private String shortDescription;
    private String description;
    private String defaultCurrency;
}
