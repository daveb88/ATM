package com.zinkwork.Atm.model;

import com.zinkwork.Atm.exception.InsufficientFundsException;
import com.zinkwork.Atm.exception.InvalidRequestException;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Repository;

@Getter
@Setter
@Repository
public class CashReserve {
    private int reserveAmount;

    private int fiftyNote;
    private int twentyNote;
    private int tenNote;
    private int fiveNote;

    public CashReserve() {
        this.fiftyNote = 10;
        this.twentyNote = 30;
        this.tenNote = 30;
        this.fiveNote = 20;

        this.reserveAmount = getCurrentCashReserve();
    }

    public CashReserve(int fiftyNote, int twentyNote, int tenNote, int fiveNote) {
        this.fiftyNote = fiftyNote;
        this.twentyNote = twentyNote;
        this.tenNote = tenNote;
        this.fiveNote = fiveNote;

        this.reserveAmount = getCurrentCashReserve();
    }

    public String withdraw(int amount) {
        String notes = "";
        if (fundsAvailable(amount)) {
            int noOfNotes;

            noOfNotes = amount/50;
            if(getFiftyNote() >= noOfNotes) {
                amount -= (noOfNotes * 50);
                setFiftyNote(getFiftyNote() - noOfNotes);
            } else {
                noOfNotes = getFiftyNote();
                amount -= (noOfNotes * 50);
                setFiftyNote(0);
            }
            notes = "£50: " + noOfNotes;

            noOfNotes = amount/20;
            if(getTwentyNote() >= noOfNotes) {
                amount -= (noOfNotes * 20);
                setTwentyNote(getTwentyNote() - noOfNotes);
            } else {
                noOfNotes = getTwentyNote();
                amount -= (noOfNotes * 20);
                setTwentyNote(0);
            }
            notes += "\n£20: " + noOfNotes;

            noOfNotes = amount/10;
            if(getTenNote() >= noOfNotes) {
                amount -= (noOfNotes * 10);
                setTenNote(getTenNote() - noOfNotes);
            } else {
                noOfNotes = getTenNote();
                amount -= (noOfNotes * 10);
                setTenNote(0);
            }
            notes += "\n£10: " + noOfNotes;

            noOfNotes = amount/5;
            if(getFiveNote() >= noOfNotes) {
                amount -= (noOfNotes * 5);
                setFiveNote(getFiveNote() - noOfNotes);
            } else {
                noOfNotes = getFiveNote();
                amount -= (noOfNotes * 5);
                setFiveNote(0);
            }
            notes += "\n£5: " + noOfNotes;

            reserveAmount = getCurrentCashReserve();
        }

        return notes;
    }

    private int getCurrentCashReserve() {
        return (getFiftyNote() * 50) + (getTwentyNote() * 20) + (getTenNote() * 10) + (getFiveNote() * 5);
    }

    public boolean fundsAvailable(int amount) {
        if (amount % 5 != 0) {
            throw new InsufficientFundsException("The amount you have requested is not available. Please enter withdrawal amount in multiples of £5");
        }
        if (reserveAmount < amount) {
            throw new InvalidRequestException("The amount you have requested is not available. Please choose a lower amount.");
        }

        if (!dispensable(amount)){
            throw new InvalidRequestException("The amount you have requested is not available. Please choose a lower amount.");
        }

        return true;
    }

    public boolean dispensable(int amount){
        int noOfNotes;

        noOfNotes = amount/50;
        if(getFiftyNote() >= noOfNotes) {
            amount -= (noOfNotes * 50);
        } else {
            amount -= (getFiftyNote() * 50);
        }

        noOfNotes = amount/20;
        if(getTwentyNote() >= noOfNotes) {
            amount -= (noOfNotes * 20);
        } else {
            amount -= (getTwentyNote() * 20);
        }

        noOfNotes = amount/10;
        if(getTenNote() >= noOfNotes) {
            amount -= (noOfNotes * 10);
        } else {
            amount -= (getTenNote() * 10);
        }

        noOfNotes = amount/5;
        if(getFiveNote() >= noOfNotes) {
            amount -= (noOfNotes * 5);
        } else {
            amount -= (getFiveNote() * 5);
        }

        if(amount == 0){
            return true;
        } else {
            return false;
        }
    }
}
