package com.makhabatusen.controller;

import com.makhabatusen.services.base.IBaseService;

import java.time.LocalDateTime;

public interface IMainController extends IBaseService {

    boolean isGroupAccommodated(int numberOfGuests, LocalDateTime startTime, int rentHours);

    boolean canAccommodateGroupTogether(int numberOfGuests);

    int getFreeSunbedsCount();

    boolean accommodateGroupSplit(int numberOfGuests, LocalDateTime startTime, int rentHours);

    void registerRejectedGroup(int numberOfGuests, int rentHours);

    boolean checkForExpiredRentTime();

    String currentOccupancy();

    String showStatistics();

    void sunbedsInfo();

    boolean deleteByGroupNum(int groupNumber);
}
