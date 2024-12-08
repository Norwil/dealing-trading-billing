package com.dealing.billing.repository;

import com.dealing.billing.model.FeeSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FeeScheduleRepository extends JpaRepository<FeeSchedule, Long> {
    Optional<FeeSchedule> findByAccount_AccountKey(String accountKey);
}
