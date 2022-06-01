package com.zinkwork.Atm.model;

import com.zinkwork.Atm.exception.InsufficientFundsException;
import com.zinkwork.Atm.exception.ResourceNotFoundException;
import lombok.Getter;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Getter
@Repository
public class Bank {
    List<Account> accountList = new ArrayList<>();

    public Bank() {
        Account account1 = new Account("123456789", "1234", 800, -200);
        Account account2 = new Account("987654321", "4321", 1230, -150);

        accountList.add(account1);
        accountList.add(account2);
    }

    public Bank(List<Account> accountList) {
        this.accountList = accountList;
    }

    public boolean validatePin(String accountNumber, String pin) {
        return accountList.stream()
                .filter(account -> accountNumber.equals(account.getAccountNumber()))
                .anyMatch(account -> pin.equals(account.getPin()));
    }

    public Account getAccount(String accountId) {
        try {
            return accountList.stream()
                    .filter(account -> accountId.equals(account.getAccountNumber()))
                    .findFirst().get();
        } catch (NoSuchElementException e) {
            throw new ResourceNotFoundException("Account not found");
        }
    }

    public boolean checkFunds(String accountNumber, Integer amount) {
        int currentOpeningBalance = getAccount(accountNumber).getBalance();
        int overdraftLimit = getAccount(accountNumber).getOverdraftLimit();

        int updatedBalance = currentOpeningBalance - amount;
        if (updatedBalance >= overdraftLimit) {
            return true;
        } else {
            throw new InsufficientFundsException("You have insufficient funds in your account.");
        }
    }

    public Integer withdrawFunds(String accountNumber, Integer amount) {
            for (Account acc : accountList) {
                if (accountNumber.equals(acc.getAccountNumber())) {
                    acc.setBalance(acc.getBalance() - amount);
                    return acc.getBalance();
                }
            }
        return null;
    }
}
