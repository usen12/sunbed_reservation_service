package com.makhabatusen.services.reception;

import com.makhabatusen.factory.Factory;
import com.makhabatusen.models.Guest;
import com.makhabatusen.models.Sunbed;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


class ReceptionServiceImplTest {

    private IReceptionService service;
    private ArrayList<Guest> guests;
    private ArrayList<Sunbed> sunbeds;

    @BeforeEach
    void setUp() {
        service = Factory.receptionService();
        Factory.sunbedService().reset();
        Factory.guestService().reset();
        guests = new ArrayList<>();
        sunbeds = new ArrayList<>();
    }

    @AfterEach
    void tearDown() {
        guests.clear();
        sunbeds.clear();
    }

    @Test
    @DisplayName("Adds sunbeds to Reserved List when there are enough sunbeds available for Reservation")
    void reservedListOfSunbeds() {
        // given
        LocalDateTime startTime = LocalDateTime.now();
        int rentHours = 2;
        int groupID = 1;
        int guestId = 1;
        for (int i = 0; i < 10; i++) {
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

        int sunbedId = 1;
        for (int i = 0; i < 15; i++) {
            Sunbed newSunbed = Sunbed.Builder.builder()
                    .id(sunbedId)
                    .isOccupied(false)
                    .build();
            sunbeds.add(newSunbed);
            sunbedId++;
        }

        // when
        List<Sunbed> reservedList = service.reservedListOfSunbeds(this.sunbeds, guests);

        // then: correct count returned and all are free sunbeds from our list
        assertThat(reservedList.size()).isEqualTo(guests.size());
        reservedList.forEach(s -> assertThat(sunbeds.contains(s)).isTrue());
    }


    @Test
    @DisplayName("Returns empty Reserved List when there are NOT enough sunbeds for Reservation")
    void reservedListIsEmpty() {
        // given
        LocalDateTime startTime = LocalDateTime.now();
        int rentHours = 2;
        int groupID = 1;
        int guestId = 1;
        for (int i = 0; i < 10; i++) {
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

        int sunbedId = 1;
        for (int i = 0; i < 5; i++) {
            Sunbed newSunbed = Sunbed.Builder.builder()
                    .id(sunbedId)
                    .isOccupied(false)
                    .build();
            sunbeds.add(newSunbed);
            sunbedId++;
        }

        // when
        List<Sunbed> reservedList = service.reservedListOfSunbeds(this.sunbeds, guests);

        // then
        assertThat(reservedList.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("Returns empty list for a group when no consecutive free sunbeds exist")
    void groupRefusedWhenNoConsecutiveSunbedsAvailable() {
        // given: alternating occupied/free — no 2 consecutive free sunbeds
        LocalDateTime startTime = LocalDateTime.now();
        // group of 2 needs 2 consecutive free sunbeds
        for (int i = 0; i < 2; i++) {
            guests.add(Guest.Builder.builder()
                    .id(i + 1).groupId(1)
                    .startTime(startTime).endTime(startTime.plusHours(2)).rentHours(2)
                    .build());
        }

        // 6 sunbeds: occupied, free, occupied, free, occupied, free — never 2 free in a row
        for (int i = 1; i <= 6; i++) {
            sunbeds.add(Sunbed.Builder.builder()
                    .id(i)
                    .isOccupied(i % 2 != 0)
                    .build());
        }

        // when
        List<Sunbed> reservedList = service.reservedListOfSunbeds(this.sunbeds, guests);

        // then
        assertThat(reservedList.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("Single guest is assigned a random free sunbed")
    void singleGuestGetsAFreeSunbed() {
        // given
        LocalDateTime startTime = LocalDateTime.now();
        guests.add(Guest.Builder.builder()
                .id(1).groupId(1)
                .startTime(startTime).endTime(startTime.plusHours(1)).rentHours(1)
                .build());

        for (int i = 1; i <= 10; i++) {
            sunbeds.add(Sunbed.Builder.builder().id(i).isOccupied(false).build());
        }

        // when
        List<Sunbed> reservedList = service.reservedListOfSunbeds(this.sunbeds, guests);

        // then: exactly one sunbed assigned, and it belongs to the list of free sunbeds
        assertThat(reservedList.size()).isEqualTo(1);
        assertThat(sunbeds.contains(reservedList.get(0))).isTrue();
    }
}
