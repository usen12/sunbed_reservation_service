package com.makhabatusen.services.sunbed;

import com.makhabatusen.models.Guest;
import com.makhabatusen.models.Sunbed;
import com.makhabatusen.repository.sunbed.ISunbedRepository;
import com.makhabatusen.utils.StandardMessages;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class SunbedServiceImpl implements ISunbedService {

    private final ISunbedRepository sunbedRepository;
    private int sunbedIdCounter = 1;

    public SunbedServiceImpl(ISunbedRepository sunbedRepository) {
        this.sunbedRepository = sunbedRepository;
    }

    @Override
    public boolean buySunbed(int quantity) {
        for (int i = 0; i < quantity; i++) {
            Sunbed newSunbed = Sunbed.Builder.builder()
                    .id(sunbedIdCounter)
                    .isOccupied(false)
                    .build();
            sunbedRepository.save(newSunbed);
            sunbedIdCounter++;
        }
        return true;
    }

    @Override
    public List<Sunbed> getSunbeds() {
        return sunbedRepository.findAll();
    }

    @Override
    public List<Sunbed> getOccupiedSunbedList() {
        return sunbedRepository.findAll().stream()
                .filter(Sunbed::isOccupied)
                .collect(Collectors.toList());
    }

    @Override
    public List<Sunbed> getUnoccupiedSunbedList() {
        return sunbedRepository.findAll().stream()
                .filter(sunbed -> !sunbed.isOccupied())
                .collect(Collectors.toList());
    }

    @Override
    public int getFreeSunbedsCount() {
        return getUnoccupiedSunbedList().size();
    }

    /**
     * Updates the Info in the Initial Sunbeds List after getting the  Reserved list of Sunbeds
     */
    @Override
    public boolean updateSunbedsList(List<Sunbed> sunbedsReserved, List<Guest> guests) {
        for (Sunbed s1 : sunbedsReserved) {
            sunbedRepository.findById(s1.getId()).ifPresent(s -> {
                s.setOccupied(true);
                s.setGroupID(guests.get(0).getGroupId());
                s.setEndOfOccupyingTime(guests.get(0).getEndOfOccupyingTime());
                sunbedRepository.save(s);
            });
        }

        logInfo("Total Occupied:  " + sunbedsReserved.size());
        return true;
    }


    /**
     * Checks the Occupied sunbeds list for Guests with expired renting time
     */
    @Override
    public HashSet<Integer> updateRentTimeForGroups() {
        logInfo(StandardMessages.DIVIDER);

        HashSet<Integer> groupsWithExpiredRentTime = new HashSet<>();
        for (Sunbed s : getOccupiedSunbedList()) {
            if (LocalDateTime.now().isAfter(s.getEndOfOccupyingTime())) {
                groupsWithExpiredRentTime.add(s.getGroupID());
            }
        }
        return groupsWithExpiredRentTime;
    }

    /**
     * Frees up the sunbeds for specific Group
     */
    @Override
    public boolean deleteByGroupNumber(int groupNumber) {
        logInfo(StandardMessages.DIVIDER);

        boolean isDeleted = false;
        int totalDeleted = 0;

        List<Sunbed> occupiedSunbeds = getOccupiedSunbedList();
        if (occupiedSunbeds.isEmpty())
            logInfo(StandardMessages.SUNBEDS_ARE_NOT_OCCUPIED);
        for (Sunbed s : occupiedSunbeds) {
            if (s.getGroupID() == groupNumber) {
                logInfo(StandardMessages.THANKS + s.getId());
                s.setOccupied(false);
                s.setGroupID(0);
                s.setEndOfOccupyingTime(null);
                sunbedRepository.save(s);
                isDeleted = true;
                totalDeleted++;
            }
        }
        logInfo("Total Deleted: " + totalDeleted);
        return isDeleted;
    }


    /**
     * Gives Info about Occupied Sunbeds
     */
    @Override
    public String currentOccupancyOfSunbeds() {
        logInfo(StandardMessages.DIVIDER);
        List<Sunbed> occupiedSunbeds = getOccupiedSunbedList();
        occupiedSunbeds.forEach(sunbed -> logInfo(sunbed.toString()));
        return "\n============================" +
                "\n Total number of sunbeds: " + sunbedRepository.findAll().size()
                + "\n Occupied sunbeds:  " + occupiedSunbeds.size()
                + "\n Free to use sunbeds: " + (sunbedRepository.findAll().size() - occupiedSunbeds.size());
    }

    public void sunbedsInfo() {
        List<Sunbed> allSunbeds = sunbedRepository.findAll();
        for (int i = 0; i < allSunbeds.size(); i++) {
            if (allSunbeds.get(i).isOccupied())
                logInfo("Sunbed # " + allSunbeds.get(i).getId() + " is Occupied: by Group # " +
                        allSunbeds.get(i).getGroupID());
            else
                logInfo("Sunbed # " + allSunbeds.get(i).getId() + " is Not Occupied");
        }
    }

    @Override
    public void reset() {
        sunbedRepository.deleteAll();
        this.sunbedIdCounter = 1;
    }
}
