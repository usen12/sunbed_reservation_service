package com.makhabatusen.services.accounting;

import com.makhabatusen.services.base.IBaseService;

public interface IAccountingService extends IBaseService {
    int HOURLY_RATE = 20;

    double getTotalRevenue();

    int getTotalOccupied();

    double getTotalLostRevenue();

    void setTotalRevenue(double totalRevenue);

    double payment(int rentHours, int guestsNumber);

    void updateTotalOccupationStat(int guestsNumber);

    void addLostRevenue(int lostHours, int guestCount);

    void incrementTotalGreeted(int count);

    String showStatistics();
}
