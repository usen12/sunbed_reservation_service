package com.makhabatusen.services.guest;

import com.makhabatusen.exceptions.ValidationException;
import com.makhabatusen.factory.Factory;
import com.makhabatusen.models.Guest;
import org.junit.jupiter.api.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.junit.jupiter.api.Assertions.*;

class GuestServiceImplTest {

    private IGuestService service;
    private ArrayList<Guest> guests;

    @BeforeEach
    void setUp() {
        service = Factory.guestService();
        service.reset();
        guests = new ArrayList<>();
    }

    @AfterEach
    void tearDown() {
        guests.clear();
    }

    @Test
    @DisplayName("adds new Group when Positive Number is passed")
    void greetNewGroup() {
        // given
        int numberOfGuests = 10;
        int rentHours = 3;
        LocalDateTime startTime = LocalDateTime.now();
        int groupID = 1;
        int guestId = 1;

        for (int i = 0; i < numberOfGuests; i++) {
            Guest guest = Guest.Builder.builder()
                    .id(guestId)
                    .groupId(groupID)
                    .startTime(startTime)
                    .endTime(startTime.plusHours(rentHours))
                    .rentHours(rentHours)
                    .build();
            guests.add(guest);
            guestId++;
        }

        // when
        List<Guest> resultList = service.greetNewGroup(numberOfGuests, startTime, rentHours);

        // then
        assertThat(resultList).isEqualTo(guests);
    }


    @Test
    @DisplayName("does not add new Group when Negative numbers or 0 is Passed")
    void doesNotAddNewGroup() {
        //given
        int numberOfGuests = -1;
        int rentHours = -1;
        LocalDateTime startTime = LocalDateTime.now();

        Assertions.assertThrows(ValidationException.class, () -> service.greetNewGroup(numberOfGuests, startTime, rentHours));
    }
}