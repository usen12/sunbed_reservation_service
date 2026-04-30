package com.makhabatusen.services.sunbed;

import com.makhabatusen.factory.Factory;
import com.makhabatusen.models.Guest;
import com.makhabatusen.models.Sunbed;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.BDDAssumptions.given;

class SunbedServiceImplTest {
    private ISunbedService service;
    private List<Sunbed> sunbeds;
    private List<Guest> guests;

    @BeforeEach
    void setUp() {
        service = Factory.sunbedService();
        service.reset();
        sunbeds = new ArrayList<>();
        guests = new ArrayList<>();
    }

    @AfterEach
    void tearDown() {
        sunbeds.clear();
        guests.clear();
    }

    @ParameterizedTest
    @CsvSource({"5", "42", "10"})
    @DisplayName("adds new Sunbeds to Sunbeds list")
    void buySunbed(int quantity) {
        given(service.buySunbed(quantity)).isTrue();

    }

    @Test
    @DisplayName("Gets the Sunbed list")
    void getSunbeds() {
        service.buySunbed(1);
        Sunbed expectedSunbed = service.getSunbeds().get(0);
        sunbeds.add(expectedSunbed);

        // then
        List<Sunbed> actual = service.getSunbeds();
        assertThat(actual).isEqualTo(sunbeds);
        assertThat(actual.size()).isEqualTo(sunbeds.size());
    }

    @Test
    @DisplayName("returns the List with Occupied Sunbeds")
    void getOccupiedSunbedList() {
        // given
        int unoccupiedQuantity = 10;
        int occupiedQuantity = 5;

        service.buySunbed(occupiedQuantity + unoccupiedQuantity);


        List<Sunbed> sunbedsFromService = service.getSunbeds();

        for (int i = 0; i < occupiedQuantity; i++) {
            sunbedsFromService.get(i).setOccupied(true);
        }

        // when
        List<Sunbed> result = service.getOccupiedSunbedList();

        // then
        assertThat(result.size() == occupiedQuantity).isTrue();
    }

    @Test
    @DisplayName("returns the List with unoccupied Sunbeds")
    void getUnoccupiedSunbedList() {
        // given
        int occupiedQuantity = 7;
        int unoccupiedQuantity = 22;

        service.buySunbed(occupiedQuantity + unoccupiedQuantity);

        List<Sunbed> sunbedsFromService = service.getSunbeds();

        for (int i = 0; i < occupiedQuantity; i++) {
            sunbedsFromService.get(i).setOccupied(true);
        }

        // when
        List<Sunbed> result = service.getUnoccupiedSunbedList();

        // then
        assertThat(result.size() == unoccupiedQuantity).isTrue();
    }



    @Test
    @DisplayName("Updates the Info in the Initial Sunbeds List after getting " +
            "the  Reserved list of Sunbeds")
    void updateSunbedsList() {
        // given
        service.buySunbed(20);
        List<Sunbed> allSunbeds = service.getSunbeds();

        List<Sunbed> reservedList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            reservedList.add(allSunbeds.get(i));
        }

        for (int i = 0; i < 10; i++) {
            Guest guest = Guest.Builder.
                    builder()
                    .id(i).
                    groupId(2)
                    .endTime(LocalDateTime.now().plusHours(2)).
                    build();
            guests.add(guest);
        }

        // when
        boolean result = service.updateSunbedsList(reservedList, guests);

        // then
        assertThat(result).isTrue();
        assertThat(service.getOccupiedSunbedList().size() == reservedList.size()).isTrue();


    }

    @Test
    @DisplayName("Returns a Hashset with Group Numbers with Expired Time")
    void updateRentTimeForGroups() {

        // given
        int notOccupied = 40;
        int occupied = 20;

        service.buySunbed(notOccupied + occupied);
        List<Sunbed> allSunbeds = service.getSunbeds();

        for (int i = 0; i < notOccupied; i++) {
            Sunbed s = allSunbeds.get(i);
            s.setOccupied(true);
            s.setGroupID(1);
            s.setEndOfOccupyingTime(LocalDateTime.of(2022, 5, 29, 1, 10));
            Factory.sunbedRepository().save(s);
        }

        for (int i = notOccupied; i < notOccupied + occupied; i++) {
            Sunbed s = allSunbeds.get(i);
            s.setOccupied(true);
            s.setGroupID(2);
            s.setEndOfOccupyingTime(LocalDateTime.of(2022, 5, 29, 1, 10));
            Factory.sunbedRepository().save(s);
        }

        // when
        HashSet<Integer> groupsToDelete = service.updateRentTimeForGroups();

        // then
        assertThat(groupsToDelete.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("deletes Group if the Group number is Correct")
    void deleteByGroupNumber() {
        // given
        service.buySunbed(25);
        List<Sunbed> allSunbeds = service.getSunbeds();

        List<Sunbed> reservedList = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            reservedList.add(allSunbeds.get(i));
        }
        for (int i = 0; i < 10; i++) {
            Guest guest = Guest.Builder.
                    builder()
                    .id(i).
                    groupId(2)
                    .endTime(LocalDateTime.now().plusHours(2)).
                    build();
            guests.add(guest);
        }
        service.updateSunbedsList(reservedList, guests);

        // when
        boolean deleteByGroupNumber = service.deleteByGroupNumber(2);


        // then
        assertThat(deleteByGroupNumber).isTrue();

    }

    @Test
    @DisplayName("deletes Nothing if the Group number is incorrect")
    void notDeleteTheGroup() {
        // given
        service.buySunbed(25);
        List<Sunbed> allSunbeds = service.getSunbeds();

        List<Sunbed> reservedList = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            reservedList.add(allSunbeds.get(i));
        }
        for (int i = 0; i < 10; i++) {
            Guest guest = Guest.Builder.
                    builder()
                    .id(i).
                    groupId(2)
                    .endTime(LocalDateTime.now().plusHours(2)).
                    build();
            guests.add(guest);
        }
        service.updateSunbedsList(reservedList, guests);

        // when
        boolean deleteByGroupNumber = service.deleteByGroupNumber(1);

        // then
        assertThat(deleteByGroupNumber).isFalse();

    }

    @Test
    @DisplayName("Displays the Information about current Occupancy of Sunbeds")
    void currentOccupancyOfSunbeds() {

        // given
        int notOccupied = 40;
        int occupied = 20;
        int total = notOccupied + occupied;

        service.buySunbed(total);
        List<Sunbed> allSunbeds = service.getSunbeds();

        for (int i = 0; i < occupied; i++) {
            Sunbed s = allSunbeds.get(i);
            s.setOccupied(true);
            s.setEndOfOccupyingTime(LocalDateTime.of(2022, 7, 30, 1, 10));
            Factory.sunbedRepository().save(s);
        }

        String expected =
                "\n============================" +
                        "\n Total number of sunbeds: " + total
                        + "\n Occupied sunbeds:  " + occupied
                        + "\n Free to use sunbeds: " + notOccupied;

        // when
        String result = service.currentOccupancyOfSunbeds();

        //then
        assertThat(result).isEqualTo(expected);
    }
}