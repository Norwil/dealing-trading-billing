package com.dealing.billing.repository;

import com.dealing.billing.model.Activity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActivityRepository extends JpaRepository<Activity, Long> {
    List<Activity> findByAccount_AccountKey(String accountKey);
}
