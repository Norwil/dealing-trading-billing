package com.dealing.billing.controller;


import com.dealing.billing.model.FeeSchedule;
import com.dealing.billing.repository.AccountRepository;
import com.dealing.billing.repository.FeeScheduleRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fee-schedules")
public class FeeScheduleController {

    private final FeeScheduleRepository feeScheduleRepository;
    private final AccountRepository accountRepository;

    public FeeScheduleController(FeeScheduleRepository feeScheduleRepository, AccountRepository accountRepository) {
        this.feeScheduleRepository = feeScheduleRepository;
        this.accountRepository = accountRepository;
    }

    @PostMapping
    public ResponseEntity<FeeSchedule> createFeeSchedule(@RequestBody FeeSchedule feeSchedule) {
        if (feeSchedule.getAccount() == null || !accountRepository.existsById(feeSchedule.getAccount().getAccountKey())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        FeeSchedule savedFeeSchedule = feeScheduleRepository.save(feeSchedule);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedFeeSchedule);
    }

    @GetMapping
    public List<FeeSchedule> getAllFeeSchedules() {
        return feeScheduleRepository.findAll();
    }

    @PutMapping("/{id}")
    public ResponseEntity<FeeSchedule> updateFeeSchedule(@PathVariable Long id, @RequestBody FeeSchedule feeSchedule) {
        return feeScheduleRepository.findById(id)
                .map(existingFeeSchedule -> {
                    existingFeeSchedule.setBps(feeSchedule.getBps());
                    existingFeeSchedule.setTradeRate(feeSchedule.getTradeRate());
                    existingFeeSchedule.setWireRate(feeSchedule.getWireRate());
                    return ResponseEntity.ok(feeScheduleRepository.save(existingFeeSchedule));
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFeeSchedule(@PathVariable Long id) {
        if (feeScheduleRepository.existsById(id)) {
            feeScheduleRepository.deleteById(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
