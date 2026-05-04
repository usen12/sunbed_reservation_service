package com.makhabatusen.services.guest;

import com.makhabatusen.factory.Factory;
import com.makhabatusen.models.Guest;
import com.makhabatusen.repository.guest.IGuestRepository;
import com.makhabatusen.services.validation.IValidationService;
import com.makhabatusen.utils.StandardMessages;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class GuestServiceImpl implements IGuestService {

    private final IGuestRepository guestRepository;
    private final IValidationService validationService;
    private int guestIdCounter = 1;
    private int groupIdCounter = 1;

    public GuestServiceImpl(IGuestRepository guestRepository) {
        this.guestRepository = guestRepository;
        this.validationService = Factory.validationService();
    }

    /**
     * Creates new Group of Guests with Specific Time and Rent Durations
     * (List of Potential guests)
     */
    @Override
    public List<Guest> greetNewGroup(int numberOfGuests, LocalDateTime startTime, int rentHours) {
        validationService.validateGuestCount(numberOfGuests);
        validationService.validateRentHours(rentHours);

        List<Guest> guests = new ArrayList<>();
        logInfo(StandardMessages.DIVIDER);

        for (int i = 0; i < numberOfGuests; i++) {
            Guest guest = Guest.Builder.builder()
                    .id(guestIdCounter)
                    .groupId(groupIdCounter)
                    .startTime(startTime)
                    .endTime(startTime.plusHours(rentHours))
                    .rentHours(rentHours)
                    .build();
            guests.add(guest);
            guestRepository.save(guest);
            guestIdCounter++;
            logInfo(guest.toString());
        }

        groupIdCounter++;
        return guests;
    }

    @Override
    public void reset() {
        guestRepository.deleteAll();
        this.guestIdCounter = 1;
        this.groupIdCounter = 1;
    }
}
