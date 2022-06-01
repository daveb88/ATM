package com.zinkwork.Atm.service;

import com.zinkwork.Atm.model.Account;
import com.zinkwork.Atm.model.Bank;
import com.zinkwork.Atm.model.CashReserve;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AtmServiceTest {
    private static AtmService atmService;
    private static CashReserve cashReserve;

    @BeforeEach
    public void setup() {
        List<Account> accountList = new ArrayList<>();

        Account account1 = new Account("123456789", "1234", 800, -200);
        Account account2 = new Account("987654321", "4321", 1230, -150);
        Account account3 = new Account("123451234", "1596", 1600, 0);

        accountList.add(account1);
        accountList.add(account2);
        accountList.add(account3);

        Bank bank = new Bank(accountList);
        cashReserve = new CashReserve(10, 30, 30, 20);
        atmService = new AtmService(bank, cashReserve);
    }

    @Test
    void validatePin() {
        assertTrue(atmService.validatePin("123456789", "1234"));
    }

    @Test
    void validatePin_invalidPin() {
        try {
            atmService.validatePin("123456789", "1204");
        } catch (RuntimeException e) {
            assertEquals("Incorrect PIN, Please try again", e.getMessage());
        }
    }

    @Test
    void getAccount() {
        assertEquals("AccountInfo {Account Number: 123456789Opening Balance: £800Overdraft: £-200}", atmService.getAccount("123456789", "1234").toString());
        assertEquals("AccountInfo {Account Number: 987654321Opening Balance: £1230Overdraft: £-150}", atmService.getAccount("987654321", "4321").toString());
        assertEquals("AccountInfo {Account Number: 123451234Opening Balance: £1600Overdraft: £0}", atmService.getAccount("123451234", "1596").toString());
    }

    @Test
    void fundsAvailable() {
        assertTrue(atmService.fundsAvailable("123456789", "1234", 500));
        assertTrue(atmService.fundsAvailable("987654321", "4321", 500));
        assertTrue(atmService.fundsAvailable("123451234", "1596", 500));
    }

    @Test
    void fundsAvailable_insufficientFunds() {
        try {
            atmService.fundsAvailable("123456789", "1234", 1300);
        } catch (RuntimeException e) {
            assertEquals("You have insufficient funds in your account.", e.getMessage());
        }
    }

    @Test
    void fundsAvailable_insufficientReserve() {
        try {
            atmService.fundsAvailable("123451234", "1596", 1550);
        } catch (RuntimeException e) {
            assertEquals("The amount you have requested is not available. Please choose a lower amount.", e.getMessage());
        }
    }

    @Test
    void withdrawFunds() {
        assertEquals("Success: You're balance has been updated\n" +
                "New Balance: £250\n" +
                "Notes dispensed:\n" +
                "£50: 10\n" +
                "£20: 30\n" +
                "£10: 25\n" +
                "£5: 0", atmService.withdrawFunds("123451234", "1596", 1350));
        assertEquals(250, atmService.getAccount("123451234", "1596").getBalance());
        assertEquals(150, cashReserve.getReserveAmount());
    }

    @Test
    void withdrawFunds_invalidPin() {
        try {
            atmService.withdrawFunds("123451234", "1536", 1350);
        } catch (RuntimeException e) {
            assertEquals("Incorrect PIN, Please try again", e.getMessage());
        }
    }

    @Test
    void withdrawFunds_insufficientFunds() {
        try {
            atmService.withdrawFunds("123456789", "1234", 1100);
        } catch (RuntimeException e) {
            assertEquals("You have insufficient funds in your account.", e.getMessage());
        }
    }

    @Test
    void withdrawFunds_insufficientReserves() {
        try {
            atmService.withdrawFunds("123451234", "1596", 1550);
        } catch (RuntimeException e) {
            assertEquals("The amount you have requested is not available. Please choose a lower amount.", e.getMessage());
        }

    }
}