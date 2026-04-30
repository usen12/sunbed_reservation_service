package com.makhabatusen.controller;

import com.makhabatusen.models.Guest;
import com.makhabatusen.models.Sunbed;
import com.makhabatusen.services.accounting.IAccountingService;
import com.makhabatusen.services.guest.IGuestService;
import com.makhabatusen.services.reception.IReceptionService;
import com.makhabatusen.services.sunbed.ISunbedService;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;

public class MainControllerImpl implements IMainController {

    private final IGuestService guestService;
    private final ISunbedService sunbedService;
    private final IReceptionService receptionService;
    private final IAccountingService accountingService;

    public MainControllerImpl(IGuestService guestService, ISunbedService sunbedService,
                              IReceptionService receptionService, IAccountingService accountingService) {
        this.guestService = guestService;
        this.sunbedService = sunbedService;
        this.receptionService = receptionService;
        this.accountingService = accountingService;
    }

    @Override
    public boolean isGroupAccommodated(int numberOfGuests, LocalDateTime startTime, int rentHours) {
        List<Guest> newGroup = guestService.greetNewGroup(numberOfGuests, startTime, rentHours);
        if (newGroup.isEmpty()) return false;

        accountingService.incrementTotalGreeted(newGroup.size());
        List<Sunbed> reservableSunbeds = receptionService.reservedListOfSunbeds(sunbedService.getSunbeds(), newGroup);

        if (reservableSunbeds.size() >= newGroup.size()) {
            sunbedService.updateSunbedsList(reservableSunbeds, newGroup);
            System.out.println("Guests have paid: " + accountingService.payment(newGroup.get(0).getRentHours(), newGroup.size()));
            accountingService.updateTotalOccupationStat(newGroup.size());
            return true;
        }

        // Caller is responsible for accounting when this returns false.
        return false;
    }

    @Override
    public boolean canAccommodateGroupTogether(int numberOfGuests) {
        return receptionService.canAccommodateConsecutively(sunbedService.getSunbeds(), numberOfGuests);
    }

    @Override
    public int getFreeSunbedsCount() {
        return sunbedService.getFreeSunbedsCount();
    }

    @Override
    public boolean accommodateGroupSplit(int numberOfGuests, LocalDateTime startTime, int rentHours) {
        List<Guest> newGroup = guestService.greetNewGroup(numberOfGuests, startTime, rentHours);
        if (newGroup.isEmpty()) return false;

        accountingService.incrementTotalGreeted(newGroup.size());
        List<Sunbed> anySunbeds = receptionService.reserveInBestBlocks(sunbedService.getSunbeds(), newGroup.size());

        if (anySunbeds.size() >= newGroup.size()) {
            sunbedService.updateSunbedsList(anySunbeds, newGroup);
            System.out.println("Guests have paid: " + accountingService.payment(newGroup.get(0).getRentHours(), newGroup.size()));
            accountingService.updateTotalOccupationStat(newGroup.size());
            return true;
        }

        accountingService.addLostRevenue(newGroup.get(0).getRentHours(), newGroup.size());
        return false;
    }

    @Override
    public void registerRejectedGroup(int numberOfGuests, int rentHours) {
        accountingService.incrementTotalGreeted(numberOfGuests);
        accountingService.addLostRevenue(rentHours, numberOfGuests);
    }

    @Override
    public boolean checkForExpiredRentTime() {
        HashSet<Integer> groupsWithExpiredTime = sunbedService.updateRentTimeForGroups();
        if (!groupsWithExpiredTime.isEmpty()) {
            groupsWithExpiredTime.forEach(sunbedService::deleteByGroupNumber);
            return true;
        }
        return false;
    }

    @Override
    public String currentOccupancy() {
        return sunbedService.currentOccupancyOfSunbeds();
    }

    @Override
    public String showStatistics() {
        return accountingService.showStatistics();
    }

    @Override
    public void sunbedsInfo() {
        sunbedService.sunbedsInfo();
    }

    @Override
    public boolean deleteByGroupNum(int groupNumber) {
        return sunbedService.deleteByGroupNumber(groupNumber);
    }
}
