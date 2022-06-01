package com.zinkwork.Atm.model;

import lombok.Builder;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Builder
public class Account {
    private final String accountNumber;
    private final String pin;
    private final BigDecimal openingBalance;
    private final BigDecimal overdraft;

    @Override
    public String toString() {
        StringBuilder accountInfo = new StringBuilder();

        accountInfo.append("AccountInfo {");
        accountInfo.append("Account Number: ").append(accountNumber);
        accountInfo.append("Opening Balance: £").append(openingBalance.setScale(2, RoundingMode.HALF_EVEN));
        accountInfo.append("Overdraft: £").append(overdraft.setScale(2, RoundingMode.HALF_EVEN));
        accountInfo.append("}");

        return accountInfo.toString();
    }

}
