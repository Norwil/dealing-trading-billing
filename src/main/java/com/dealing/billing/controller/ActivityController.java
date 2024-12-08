package com.dealing.billing.controller;


import com.dealing.billing.model.Activity;
import com.dealing.billing.repository.AccountRepository;
import com.dealing.billing.repository.ActivityRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/activities")
public class ActivityController {

    private final ActivityRepository activityRepository;
    private final AccountRepository accountRepository;

    public ActivityController(ActivityRepository activityRepository, AccountRepository accountRepository) {
        this.activityRepository = activityRepository;
        this.accountRepository = accountRepository;
    }

    @PostMapping
    public ResponseEntity<Activity> createActivity(@RequestBody Activity activity) {
        if (activity.getAccount() == null || !accountRepository.existsById(activity.getAccount().getAccountKey())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        Activity savedActivity = activityRepository.save(activity);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedActivity);
    }

    @GetMapping("/{accountKey}")
    public ResponseEntity<List<Activity>> getActivitiesByAccountKey(@PathVariable String accountKey) {
        if (!accountRepository.existsById(accountKey)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        List<Activity> activities = activityRepository.findByAccount_AccountKey(accountKey);
        return ResponseEntity.ok(activities);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteActivity(@PathVariable Long id) {
        if (activityRepository.existsById(id)) {
            activityRepository.deleteById(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping
    public List<Activity> getAllActivities() {
        return activityRepository.findAll();
    }

}
