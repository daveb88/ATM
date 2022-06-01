package com.zinkwork.Atm.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CashReserveTest {
    private static CashReserve reserve;

    @BeforeEach
    public void setup(){
        reserve = new CashReserve(10, 30, 30, 20);
    }

    @Test
    void withdraw_300() {
        assertEquals(1500, reserve.getReserveAmount());
        assertEquals(10, reserve.getFiftyNote());
        assertEquals(30, reserve.getTwentyNote());
        assertEquals(30, reserve.getTenNote());
        assertEquals(20, reserve.getFiveNote());

        reserve.withdraw(300);

        assertEquals(1200, reserve.getReserveAmount());
        assertEquals(4, reserve.getFiftyNote());
        assertEquals(30, reserve.getTwentyNote());
        assertEquals(30, reserve.getTenNote());
        assertEquals(20, reserve.getFiveNote());
    }

    @Test
    void withdraw_1250() {
        assertEquals(1500, reserve.getReserveAmount());
        assertEquals(10, reserve.getFiftyNote());
        assertEquals(30, reserve.getTwentyNote());
        assertEquals(30, reserve.getTenNote());
        assertEquals(20, reserve.getFiveNote());

        reserve.withdraw(1250);

        assertEquals(250, reserve.getReserveAmount());
        assertEquals(0, reserve.getFiftyNote());
        assertEquals(0, reserve.getTwentyNote());
        assertEquals(15, reserve.getTenNote());
        assertEquals(20, reserve.getFiveNote());
    }

    @Test
    void withdraw_invalidAmount() {
        try {
            reserve.withdraw(1123);
        } catch (RuntimeException e) {
            assertEquals("The amount you have requested is not available. Please enter withdrawal amount in multiples of £5", e.getMessage());
        }
    }

    @Test
    void withdraw_notEnoughFunds() {
        try {
            reserve.withdraw(2000);
        } catch (RuntimeException e) {
            assertEquals("The amount you have requested is not available. Please choose a lower amount.", e.getMessage());
        }
    }
}