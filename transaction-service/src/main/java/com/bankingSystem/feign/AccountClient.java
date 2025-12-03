package com.bankingSystem.feign;

import com.bankingSystem.feign.dto.AccountDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(name = "account-service")
public interface AccountClient {

    @GetMapping("/api/accounts/{accNo}")
    AccountDTO getAccount(@PathVariable String accNo);

    @PutMapping("/api/accounts/{accNo}/balance")
    AccountDTO updateBalance(@PathVariable String accNo,
                             @RequestBody Map<String, Double> body);
}
