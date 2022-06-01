package com.zinkwork.Atm.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class Account {
    private final String accountNumber;
    private final String pin;
    private Integer balance;
    private Integer overdraftLimit;

    public Account(String accountNumber, String pin, Integer balance, Integer overdraftLimit){
        this.accountNumber = accountNumber;
        this.pin = pin;
        this.balance = balance;
        this.overdraftLimit = overdraftLimit;

    }
    @Override
    public String toString() {
        StringBuilder accountInfo = new StringBuilder();

        accountInfo.append("AccountInfo {");
        accountInfo.append("Account Number: ").append(accountNumber);
        accountInfo.append("Opening Balance: £").append(balance);
        accountInfo.append("Overdraft: £").append(overdraftLimit);
        accountInfo.append("}");

        return accountInfo.toString();
    }
}
