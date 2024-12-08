package com.dealing.billing.controller;


import com.dealing.billing.model.Account;
import com.dealing.billing.repository.AccountRepository;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountRepository accountRepository;

    public AccountController(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @PostMapping
    public ResponseEntity<Account> createAccount(@RequestBody Account account) {
        if (accountRepository.findById(account.getAccountKey()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        Account savedAccount = accountRepository.save(account);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedAccount);
    }

    @GetMapping
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    @GetMapping("/{accountKey}")
    public ResponseEntity<Account> getAccountByAccountKey(@PathVariable String accountKey) {
        return accountRepository.findById(accountKey)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PutMapping("/{accountKey}")
    public ResponseEntity<Account> updateAccount(@PathVariable String accountKey, @RequestBody Account updatedAccount) {
        return accountRepository.findById(accountKey)
                .map(account -> {
                    account.setClientName(updatedAccount.getClientName());
                    return ResponseEntity.ok(accountRepository.save(account));
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @DeleteMapping("/{accountKey}")
    public ResponseEntity<Void> deleteAccount(@PathVariable String accountKey) {
        if (!accountRepository.existsById(accountKey)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        accountRepository.deleteById(accountKey);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
