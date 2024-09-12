package prod.bookapp.wraper;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Component;

@NoArgsConstructor
@Getter
@Setter
@ToString
@Component
public class ApiResponse<T> {
    private String message;
    private T response;

    public ApiResponse(String message, T response) {
        this.message = message;
        this.response = response;
    }

    public ApiResponse(String message) {
        this.message = message;
    }
}
