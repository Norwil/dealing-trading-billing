package com.dealing.billing.service;

import com.dealing.billing.model.Activity;
import com.dealing.billing.model.FeeSchedule;
import com.dealing.billing.repository.ActivityRepository;
import com.dealing.billing.repository.FeeScheduleRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class BillingService {

    private final FeeScheduleRepository feeScheduleRepository;
    private final ActivityRepository activityRepository;

    public BillingService(FeeScheduleRepository feeScheduleRepository, ActivityRepository activityRepository) {
        this.feeScheduleRepository = feeScheduleRepository;
        this.activityRepository = activityRepository;
    }

    public double calculateCustodyFee(String accountKey, LocalDate startDate, LocalDate endDate) {
        FeeSchedule feeSchedule = feeScheduleRepository.findByAccount_AccountKey(accountKey)
                .orElseThrow(() -> new IllegalArgumentException("Fee schedule not found for accountKey: " + accountKey));

        List<Activity> activities = activityRepository.findByAccount_AccountKey(accountKey);

        return activities.stream()
                .filter(activity -> "Custody".equals(activity.getActivityType()))
                .mapToDouble(activity -> {
                    long totalDays = java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate);
                    return activity.getQuantity() * feeSchedule.getBps() / 10000 * ((double) totalDays / 365);
                })
                .sum();
    }

    public double calculateTradeFee(String accountKey, LocalDate startDate, LocalDate endDate) {
        // Fetch the fee schedule associated with the account key
        FeeSchedule feeSchedule = feeScheduleRepository.findByAccount_AccountKey(accountKey)
                .orElseThrow(() -> new IllegalArgumentException("Fee schedule not found for account key: " + accountKey));

        // Fetch all activities associated with the account key
        List<Activity> activities = activityRepository.findByAccount_AccountKey(accountKey);

        // Filter activities by type "Trade" and within the specified date range, then calculate the trade fee
        return activities.stream()
                .filter(activity -> "Trade".equals(activity.getActivityType())) // Filter for "Trade" activities
                .filter(activity -> !activity.getActivityDate().isBefore(startDate) // Filter by date range
                        && !activity.getActivityDate().isAfter(endDate))
                .mapToDouble(activity -> activity.getQuantity() * feeSchedule.getTradeRate()) // Calculate trade fee
                .sum();
    }


    public double calculateWireFee(String accountKey, LocalDate startDate, LocalDate endDate) {
        FeeSchedule feeSchedule = feeScheduleRepository.findByAccount_AccountKey(accountKey)
                .orElseThrow(() -> new IllegalArgumentException("Fee schedule not found for account key: " + accountKey));
        List<Activity> activities = activityRepository.findByAccount_AccountKey(accountKey);
        return activities.stream()
                .filter(activity -> "Wire".equals(activity.getActivityType())) // Filter for "Wire" activities
                .filter(activity -> !activity.getActivityDate().isBefore(startDate) && !activity.getActivityDate().isAfter(endDate)) // Filter by date range
                .mapToDouble(activity -> activity.getQuantity() * feeSchedule.getWireRate()) // Calculate wire fee
                .sum();
    }


}
