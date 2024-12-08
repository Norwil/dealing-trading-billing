//package com.dealing.billing.controller;
//
//import com.dealing.billing.model.Account;
//import com.dealing.billing.model.Activity;
//import com.dealing.billing.model.FeeSchedule;
//import com.dealing.billing.repository.AccountRepository;
//import com.dealing.billing.repository.ActivityRepository;
//import com.dealing.billing.repository.FeeScheduleRepository;
//import com.dealing.billing.service.BillingService;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.time.LocalDate;
//import java.util.List;
//import java.util.Optional;
//
//@RestController
//@RequestMapping("/api/billing")
//public class BillingController {
//
//    private final BillingService billingService;
//    private final AccountRepository accountRepository;
//    private final FeeScheduleRepository feeScheduleRepository;
//    private final ActivityRepository activityRepository;
//
//    public BillingController(
//            BillingService billingService,
//            AccountRepository accountRepository,
//            FeeScheduleRepository feeScheduleRepository,
//            ActivityRepository activityRepository) {
//        this.billingService = billingService;
//        this.accountRepository = accountRepository;
//        this.feeScheduleRepository = feeScheduleRepository;
//        this.activityRepository = activityRepository;
//    }
//
//    // Create Account
//    @PostMapping("/accounts")
//    public ResponseEntity<Account> createAccount(@RequestBody Account account) {
//        if (accountRepository.findById(account.getAccountKey()).isPresent()) {
//            return ResponseEntity.status(HttpStatus.CONFLICT).build();
//        }
//        Account savedAccount = accountRepository.save(account);
//        return ResponseEntity.status(HttpStatus.CREATED).body(savedAccount);
//    }
//
//    // Get All Accounts
//    @GetMapping("/accounts")
//    public List<Account> getAllAccounts() {
//        return accountRepository.findAll();
//    }
//
//    // Get Account by accountKey
//    @GetMapping("/accounts/{accountKey}")
//    public ResponseEntity<Account> getAccountByAccountKey(@PathVariable String accountKey) {
//        Optional<Account> account = accountRepository.findById(accountKey);
//        return account.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
//    }
//
//    // Update Account
//    @PutMapping("/accounts/{accountKey}")
//    public ResponseEntity<Account> updateAccount(@PathVariable String accountKey, @RequestBody Account updatedAccount) {
//        return accountRepository.findById(accountKey)
//                .map(account -> {
//                    account.setClientName(updatedAccount.getClientName());
//                    return ResponseEntity.ok(accountRepository.save(account));
//                })
//                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
//    }
//
//    // Delete Account
//    @DeleteMapping("/accounts/{accountKey}")
//    public ResponseEntity<Void> deleteAccount(@PathVariable String accountKey) {
//        if (!accountRepository.existsById(accountKey)) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//        }
//
//        // Cascade delete FeeSchedules and Activities
//        feeScheduleRepository.findByAccount_AccountKey(accountKey).ifPresent(feeScheduleRepository::delete);
//        activityRepository.findByAccount_AccountKey(accountKey).forEach(activityRepository::delete);
//
//        accountRepository.deleteById(accountKey);
//        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
//    }
//
//    // Create FeeSchedule
//    @PostMapping("/fee-schedules")
//    public ResponseEntity<FeeSchedule> createFeeSchedule(@RequestBody FeeSchedule feeSchedule) {
//        if (feeSchedule.getAccount() == null || !accountRepository.existsById(feeSchedule.getAccount().getAccountKey())) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
//        }
//        FeeSchedule savedFeeSchedule = feeScheduleRepository.save(feeSchedule);
//        return ResponseEntity.status(HttpStatus.CREATED).body(savedFeeSchedule);
//    }
//
//    // Update FeeSchedule
//    @PutMapping("/fee-schedules/{id}")
//    public ResponseEntity<FeeSchedule> updateFeeSchedule(@PathVariable Long id, @RequestBody FeeSchedule feeSchedule) {
//        return feeScheduleRepository.findById(id)
//                .map(existingFeeSchedule -> {
//                    existingFeeSchedule.setBps(feeSchedule.getBps());
//                    existingFeeSchedule.setTradeRate(feeSchedule.getTradeRate());
//                    existingFeeSchedule.setWireRate(feeSchedule.getWireRate());
//                    FeeSchedule updatedFeeSchedule = feeScheduleRepository.save(existingFeeSchedule);
//                    return ResponseEntity.ok(updatedFeeSchedule);
//                })
//                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
//    }
//
//    // Delete FeeSchedule
//    @DeleteMapping("/fee-schedules/{id}")
//    public ResponseEntity<Void> deleteFeeSchedule(@PathVariable Long id) {
//        if (feeScheduleRepository.existsById(id)) {
//            feeScheduleRepository.deleteById(id);
//            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
//        }
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//    }
//
//    // Get All FeeSchedules
//    @GetMapping("/fee-schedules")
//    public List<FeeSchedule> getAllFeeSchedules() {
//        return feeScheduleRepository.findAll();
//    }
//
//    // Get All Activities
//    @GetMapping("/activities")
//    public List<Activity> getAllActivities() {
//        return activityRepository.findAll();
//    }
//
//    // Create Activity
//    @PostMapping("/activities")
//    public ResponseEntity<Activity> createActivity(@RequestBody Activity activity) {
//        if (activity.getAccount() == null || !accountRepository.existsById(activity.getAccount().getAccountKey())) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
//        }
//        Activity savedActivity = activityRepository.save(activity);
//        return ResponseEntity.status(HttpStatus.CREATED).body(savedActivity);
//    }
//
//    // Get All Activities for an Account
//    @GetMapping("/activities/{accountKey}")
//    public ResponseEntity<List<Activity>> getActivitiesByAccountKey(@PathVariable String accountKey) {
//        if (!accountRepository.existsById(accountKey)) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//        }
//        List<Activity> activities = activityRepository.findByAccount_AccountKey(accountKey);
//        return ResponseEntity.ok(activities);
//    }
//
//    // Delete Activity
//    @DeleteMapping("/activities/{id}")
//    public ResponseEntity<Void> deleteActivity(@PathVariable Long id) {
//        if (activityRepository.existsById(id)) {
//            activityRepository.deleteById(id);
//            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
//        }
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//    }
//
//    // Calculate Custody Fee
//    @GetMapping("/calculate-custody")
//    public ResponseEntity<Double> calculateCustodyFee(
//            @RequestParam String accountKey,
//            @RequestParam String startDate,
//            @RequestParam String endDate) {
//        try {
//            LocalDate start = LocalDate.parse(startDate);
//            LocalDate end = LocalDate.parse(endDate);
//            double custodyFee = billingService.calculateCustodyFee(accountKey, start, end);
//            return ResponseEntity.ok(custodyFee);
//        } catch (IllegalArgumentException e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
//        }
//    }
//
//
//}
