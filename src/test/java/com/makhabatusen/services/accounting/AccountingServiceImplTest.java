package com.makhabatusen.services.accounting;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class AccountingServiceImplTest {

    IAccountingService service;

    @BeforeEach
    void setUp() {
        service = new AccountingServiceImpl();
    }

    @DisplayName("Checks guest payment")
    @ParameterizedTest
    @CsvSource({"2, 10"})
    void payment(int rentHours, int guestNumber) {
        assertThat(service.payment(rentHours, guestNumber)).isEqualTo(400);
    }

    @Test
    @DisplayName("Test for getter of Total Occupation Field")
    void updateTotalOccupationStat() {
        // given
        int totalOccupation = 230;

        // when
        service.updateTotalOccupationStat(totalOccupation);

        // then
        assertThat(service.getTotalOccupied()).isEqualTo(230);
    }

    @Test
    @DisplayName("Returns String with received Revenue and missed Revenue info")
    void showStatistics() {
        // given
        int totalOccupied = 180;
        double totalRevenue = 200;
        int totalGreeted = 200;
        int lostGuests = totalGreeted - totalOccupied;
        int lostHours = 20;

        // when
        service.setTotalRevenue(totalRevenue);
        // simulate one failed group of lostGuests guests for lostHours hours
        service.addLostRevenue(lostHours, lostGuests);
        service.updateTotalOccupationStat(totalOccupied);
        service.incrementTotalGreeted(totalGreeted);

        // then
        double expectedLoss = (double) lostHours * lostGuests * IAccountingService.HOURLY_RATE;
        String expected = "--- BEACH CLUB STATISTICS ---" +
                "\nTotal Greeted Guests:   " + totalGreeted +
                "\nTotal Occupied Sunbeds: " + totalOccupied +
                "\nTotal Lost Guests:     " + lostGuests +
                "\n----------------------------" +
                "\nTotal Revenue:         $" + totalRevenue +
                "\nTotal Potential Loss:  $" + expectedLoss +
                "\n----------------------------";

        assertThat(service.showStatistics()).isEqualTo(expected);
    }

    @Test
    @DisplayName("Test for getter and setter for Total Revenue")
    void totalRevenueCheck() {
        // given
        double totalRevenue = 100.0;

        // when
        service.setTotalRevenue(totalRevenue);

        // then
        assertThat(service.getTotalRevenue()).isEqualTo(totalRevenue);
    }

    @Test
    @DisplayName("addLostRevenue accumulates correctly across multiple failed groups")
    void addLostRevenueAccumulates() {
        // group 1: 3 guests, 2 hours → 3 * 2 * 20 = 120
        service.addLostRevenue(2, 3);
        // group 2: 5 guests, 4 hours → 5 * 4 * 20 = 400
        service.addLostRevenue(4, 5);

        assertThat(service.getTotalLostRevenue()).isEqualTo(520.0);
    }
}
