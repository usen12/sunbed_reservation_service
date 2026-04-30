package com.makhabatusen.services.guest;

import com.makhabatusen.models.Guest;
import com.makhabatusen.services.base.IBaseService;

import java.time.LocalDateTime;
import java.util.List;

public interface IGuestService extends IBaseService {
    List<Guest> greetNewGroup(int numberOfGuests, LocalDateTime startTime, int rentHours);

    void reset();

}
