package prod.bookapp.enums;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import prod.bookapp.wraper.ApiResponse;

import java.util.Collection;

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
        if (response == null) {
            return new ResponseEntity<>(new ApiResponse<>("not found"), HttpStatus.NOT_FOUND);
        }
        if (response instanceof Collection collection) {
            if (collection.isEmpty()) {
                return new ResponseEntity<>(new ApiResponse<>("not found"), HttpStatus.NOT_FOUND);
            }
        }
        if (response instanceof Object[] array) {
            if (array.length == 0) {
                return new ResponseEntity<>(new ApiResponse<>("not found"), HttpStatus.NOT_FOUND);
            }
        }
        return ResponseEntity.ok(new ApiResponse<>("Success", response));
    }
}
