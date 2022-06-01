package com.zinkwork.Atm.controller;

import com.zinkwork.Atm.model.Account;
import com.zinkwork.Atm.model.Bank;
import com.zinkwork.Atm.model.CashReserve;
import com.zinkwork.Atm.service.AtmService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AtmControllerTest {
    private static AtmService atmService;
    private static AtmController atmController;

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
        CashReserve reserve = new CashReserve(10, 30, 30, 20);
        atmService = new AtmService(bank, reserve);
        atmController = new AtmController(atmService);
    }

    @Test
    void getAccountBalance() {
        assertEquals("Account: 123456789\n" +
                "Balance: £800", atmController.getAccount("123456789", "1234"));
        assertEquals("Account: 987654321\n" +
                "Balance: £1230", atmController.getAccount("987654321", "4321"));
        assertEquals("Account: 123451234\n" +
                "Balance: £1600", atmController.getAccount("123451234", "1596"));
    }

    @Test
    void getAccountBalance_invalidPin() {
        try {
            atmController.getAccount("123456789", "1235");
        } catch (RuntimeException e) {
            assertEquals("Incorrect PIN, Please try again", e.getMessage());
        }
    }

    @Test
    void getOverdraftBalance() {
        assertEquals("Overdraft: £200", atmController.getOverdraft("123456789", "1234"));
        assertEquals("Overdraft: £150", atmController.getOverdraft("987654321", "4321"));
        assertEquals("Overdraft: £0", atmController.getOverdraft("123451234", "1596"));
    }

    @Test
    void getOverdraftBalance_invalidPin() {
        try {
            atmController.getOverdraft("123456789", "1235");
        } catch (RuntimeException e) {
            assertEquals("Incorrect PIN, Please try again", e.getMessage());
        }
    }

    @Test
    void withdrawFunds() {
        assertEquals("Success: You're balance has been updated\n" +
                "New Balance: £675\n" +
                "Notes dispensed:\n" +
                "£50: 2\n" +
                "£20: 1\n" +
                "£10: 0\n" +
                "£5: 1", atmController.getFunds("123456789", "1234", 125));
    }

    @Test
    void withdrawFunds_insufficientFunds_account() {
        try {
            atmController.getFunds("123456789", "1234", 1600);
        } catch (RuntimeException e) {
            assertEquals("You have insufficient funds in your account.", e.getMessage());
        }
    }

    @Test
    void withdrawFunds_insufficientFunds_reserves() {
        try {
            atmController.getFunds("123451234", "1596", 1550);
        } catch (RuntimeException e) {
            assertEquals("The amount you have requested is not available. Please choose a lower amount.", e.getMessage());
        }
    }

    @Test
    void withdrawFunds_invalidPin() {
        try {
            atmController.getFunds("123456789", "1275", 125);
        } catch (RuntimeException e) {
            assertEquals("Incorrect PIN, Please try again", e.getMessage());
        }
    }

}