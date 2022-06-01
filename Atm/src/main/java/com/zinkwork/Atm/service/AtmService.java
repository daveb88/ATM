package com.zinkwork.Atm.service;

import com.zinkwork.Atm.exception.AuthenticationException;
import com.zinkwork.Atm.model.Account;
import com.zinkwork.Atm.model.Bank;
import com.zinkwork.Atm.model.CashReserve;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class AtmService {
    private final Bank bank;
    private final CashReserve reserve;

    public AtmService(Bank bank, CashReserve reserve) {
        this.bank = Objects.requireNonNull(bank, "bank");
        this.reserve = Objects.requireNonNull(reserve, "reserve");
    }

    public boolean validatePin(String id, String token) {
        if (bank.validatePin(id, token)) {
            return true;
        } else {
            throw new AuthenticationException("Incorrect PIN, Please try again");
        }

    }

    public Account getAccount(String accountId, String token) {
        if (validatePin(accountId, token)) {
            return (bank.getAccount(accountId));
        } else {
            throw new AuthenticationException("Incorrect PIN, Please try again");
        }
    }

    public boolean fundsAvailable(String accountId, String token, Integer amount) {
        if (validatePin(accountId, token) && bank.checkFunds(accountId, amount) && reserve.fundsAvailable(amount)) {
            return true;
        }
        return false;
    }

    public String withdrawFunds(String accountId, String token, Integer amount) {
        if (fundsAvailable(accountId, token, amount)) {
            return "Success: You're balance has been updated" +
                    "\nNew Balance: £" + bank.withdrawFunds(accountId, amount) +
                    "\nNotes dispensed:" +
                    "\n" + reserve.withdraw(amount);
        }
        return null;
    }
}
