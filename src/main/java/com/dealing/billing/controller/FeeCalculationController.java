package com.dealing.billing.controller;


import com.dealing.billing.service.BillingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@RestController
@RequestMapping("/api/fee-calculations")
public class FeeCalculationController {

    private final BillingService billingService;

    public FeeCalculationController(BillingService billingService) {
        this.billingService = billingService;
    }

    @GetMapping("/calculate-custody")
    public ResponseEntity<Double> calculateCustodyFee(
            @RequestParam String accountKey,
            @RequestParam String startDate,
            @RequestParam String endDate) {
        try {
            LocalDate start = LocalDate.parse(startDate);
            LocalDate end = LocalDate.parse(endDate);
            double custodyFee = billingService.calculateCustodyFee(accountKey, start, end);
            return ResponseEntity.ok(custodyFee);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/calculate-trade")
    public ResponseEntity<Double> calculateTradeFee(
            @RequestParam String accountKey,
            @RequestParam String startDate,
            @RequestParam String endDate) {
        try {
            LocalDate start = LocalDate.parse(startDate); // Parse startDate
            LocalDate end = LocalDate.parse(endDate);     // Parse endDate

            // Call the service method to calculate the trade fee
            double tradeFee = billingService.calculateTradeFee(accountKey, start, end);
            return ResponseEntity.ok(tradeFee);          // Return the result
        } catch (DateTimeParseException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); // Handle date parsing error
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);   // Handle accountKey not found
        }
    }


    @GetMapping("/calculate-wire")
    public ResponseEntity<Double> calculateWireFee(
            @RequestParam String accountKey,
            @RequestParam String startDate,
            @RequestParam String endDate
    ) {
        try {
            LocalDate start = LocalDate.parse(startDate);
            LocalDate end = LocalDate.parse(endDate);
            double wireFee = billingService.calculateWireFee(accountKey, start, end);
            return ResponseEntity.ok(wireFee);
        } catch (DateTimeParseException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

}
