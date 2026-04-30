package com.makhabatusen.services.sunbed;

import com.makhabatusen.models.Guest;
import com.makhabatusen.models.Sunbed;
import com.makhabatusen.services.base.IBaseService;

import java.util.HashSet;
import java.util.List;

public interface ISunbedService extends IBaseService {

    boolean buySunbed(int quantity);

    List<Sunbed> getSunbeds();

    List<Sunbed> getOccupiedSunbedList();

    List<Sunbed> getUnoccupiedSunbedList();

    int getFreeSunbedsCount();

    boolean updateSunbedsList(List<Sunbed> sunbedsReserved, List<Guest> guests);

    HashSet<Integer> updateRentTimeForGroups();

    boolean deleteByGroupNumber(int groupNumber);

    String currentOccupancyOfSunbeds();

    void sunbedsInfo();

    void reset();
}
