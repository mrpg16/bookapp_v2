package prod.bookapp.dto;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserAuthResponseDTO {
    private String accessToken;
    private String refreshToken;
}
