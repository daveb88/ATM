package com.zinkwork.Atm.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BankTest {
    private static Bank bank;

    @BeforeEach
    public void setup() {
        List<Account> accountList = new ArrayList<>();

        Account account1 = new Account("123456789", "1234", 800, -200);
        Account account2 = new Account("987654321", "4321", 1230, -150);

        accountList.add(account1);
        accountList.add(account2);

        bank = new Bank(accountList);
    }

    @Test
    void validatePin() {
        assertTrue(bank.validatePin("123456789", "1234"));
    }

    @Test
    void validatePin_invalidPin() {
        assertFalse(bank.validatePin("123456789", "4321"));
    }

    @Test
    void getAccountBalance() {
        assertEquals(800, bank.getAccount("123456789").getBalance());
        assertEquals(1230, bank.getAccount("987654321").getBalance());
    }

    @Test
    void getAccountBalance_throwsError() {
        try {
            bank.getAccount("121212121").getBalance();
        } catch (RuntimeException e) {
            assertEquals("Account not found", e.getMessage());
        }
    }

    @Test
    void getOverdraftLimit() {
        assertEquals(-200, bank.getAccount("123456789").getOverdraftLimit());
        assertEquals(-150, bank.getAccount("987654321").getOverdraftLimit());
    }

    @Test
    void getOverdraftLimit_throwsError() {
        try {
            bank.getAccount("981654321").getOverdraftLimit();
        } catch (RuntimeException e) {
            assertEquals("Account not found", e.getMessage());
        }
    }

    @Test
    void updateBalance() {
        assertEquals(-100, bank.withdrawFunds("123456789", 900));

        assertEquals(630, bank.withdrawFunds("987654321", 600));
    }

    @Test
    void getAccountList() {
        assertEquals("123456789", (bank.getAccountList().get(0)).getAccountNumber());
        assertEquals("987654321", (bank.getAccountList().get(1)).getAccountNumber());

        assertEquals(2, bank.getAccountList().size());
    }
}