package prod.bookapp.enums;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import prod.bookapp.wraper.ApiResponse;

public class ResultWrapper {
    public static ResponseEntity<ApiResponse<Object>> getResponse(Object response) {
        if (response instanceof String strResponse) {
            if (strResponse.contains("Error:") && strResponse.contains("not found")) {
                return new ResponseEntity<>(new ApiResponse<>(strResponse), HttpStatus.NOT_FOUND);
            }
            if (strResponse.contains("Error:")) {
                return ResponseEntity.badRequest().body(new ApiResponse<>(strResponse));
            }
            return ResponseEntity.ok(new ApiResponse<>("Success", strResponse));
        }
        // Handle case where response is not a String
        return ResponseEntity.ok(new ApiResponse<>("Success", response));
    }
}
