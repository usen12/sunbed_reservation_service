package com.makhabatusen.services.accounting;

import com.makhabatusen.utils.StandardMessages;

public class AccountingServiceImpl implements IAccountingService {

    private double totalRevenue = 0;
    private int totalOccupied = 0;
    private double totalLostRevenue = 0;
    private int totalGreeted = 0;

    public double getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public int getTotalOccupied() {
        return totalOccupied;
    }

    public double getTotalLostRevenue() {
        return totalLostRevenue;
    }

    /**
     * Receives payment and updates Total revenue data
     */
    @Override
    public double payment(int rentHours, int guestsNumber) {
        int currentPayment = HOURLY_RATE * rentHours * guestsNumber;
        setTotalRevenue(getTotalRevenue() + currentPayment);
        return currentPayment;
    }

    @Override
    public void updateTotalOccupationStat(int totalOccupied) {
        this.totalOccupied += totalOccupied;
    }

    @Override
    public void addLostRevenue(int lostHours, int guestCount) {
        this.totalLostRevenue += (double) lostHours * guestCount * HOURLY_RATE;
    }


    @Override
    public void incrementTotalGreeted(int count) {
        this.totalGreeted += count;
    }


    /**
     * Returns Statistics about received Revenue and missed Revenue
     */

    @Override
    public String showStatistics() {
        logInfo(StandardMessages.DIVIDER);
        int missedGuests = totalGreeted - totalOccupied;

        return "--- BEACH CLUB STATISTICS ---" +
                "\nTotal Greeted Guests:   " + totalGreeted +
                "\nTotal Occupied Sunbeds: " + totalOccupied +
                "\nTotal Lost Guests:     " + missedGuests +
                "\n----------------------------" +
                "\nTotal Revenue:         $" + totalRevenue +
                "\nTotal Potential Loss:  $" + totalLostRevenue +
                "\n----------------------------";
    }

}
