package prod.bookapp.configuration;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import prod.bookapp.wraper.ApiResponse;
import prod.bookapp.wraper.ResultWrapper;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiResponse<Object>> handleMissingParams(MissingServletRequestParameterException ex) {
        String parameterName = ex.getParameterName();
        String message = "Required request parameter '" + parameterName + "' is missing.";
        return ResultWrapper.getResponse(message);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiResponse<Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .findFirst()
                .orElse("Invalid input.");
        return ResultWrapper.getResponse(message);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiResponse<Object>> handleIllegalArgumentException(IllegalArgumentException ex) {
        String message = ex.getMessage();
        return ResultWrapper.getResponse(message);
    }

    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<ApiResponse<Object>> handleJwtSignature(SignatureException ex) {
        String message = "Error: Token is incorrect: " + ex.getMessage();
        return ResultWrapper.getResponse(message);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ApiResponse<Object>> handleJwtExpired(SignatureException ex) {
        String message = "Error: Token is expired: " + ex.getMessage();
        return ResultWrapper.getResponse(message);
    }

//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ApiResponse<Object>> handleGenericException(Exception ex) {
//        String message = ex.getMessage();
//        return ResultWrapper.getResponse(message);
//    }


}
