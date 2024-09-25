package prod.bookapp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import prod.bookapp.enums.Enums;
import prod.bookapp.wraper.ApiResponse;
import prod.bookapp.wraper.ResultWrapper;

@RestController
@RequestMapping("/enum")
public class EnumController {
    @GetMapping("/providers")
    public ResponseEntity<ApiResponse<Object>> getAllProviders() {
        var result = Enums.getOnlineProviders();
        return ResultWrapper.getResponse(result);
    }

    @GetMapping("/currencies")
    public ResponseEntity<ApiResponse<Object>> getAllCurrencies() {
        var result = Enums.getCurrencies();
        return ResultWrapper.getResponse(result);
    }
}
