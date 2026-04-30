package com.makhabatusen.factory;

import com.makhabatusen.controller.IMainController;
import com.makhabatusen.controller.MainControllerImpl;
import com.makhabatusen.repository.guest.IGuestRepository;
import com.makhabatusen.repository.guest.InMemoryGuestRepository;
import com.makhabatusen.repository.sunbed.ISunbedRepository;
import com.makhabatusen.repository.sunbed.InMemorySunbedRepository;
import com.makhabatusen.services.accounting.AccountingServiceImpl;
import com.makhabatusen.services.accounting.IAccountingService;
import com.makhabatusen.services.guest.GuestServiceImpl;
import com.makhabatusen.services.guest.IGuestService;
import com.makhabatusen.services.reception.IReceptionService;
import com.makhabatusen.services.reception.ReceptionServiceImpl;
import com.makhabatusen.services.sunbed.ISunbedService;
import com.makhabatusen.services.sunbed.SunbedServiceImpl;
import com.makhabatusen.services.validation.IValidationService;
import com.makhabatusen.services.validation.ValidationServiceImpl;

public class Factory {

    private static final IValidationService VALIDATION_SERVICE = new ValidationServiceImpl();
    private static final IGuestRepository GUEST_REPOSITORY = new InMemoryGuestRepository();
    private static final ISunbedRepository SUNBED_REPOSITORY = new InMemorySunbedRepository();

    private static final IGuestService GUEST_SERVICE = new GuestServiceImpl(GUEST_REPOSITORY);
    private static final ISunbedService SUNBED_SERVICE = new SunbedServiceImpl(SUNBED_REPOSITORY);
    private static final IReceptionService RECEPTION_SERVICE = new ReceptionServiceImpl();
    private static final IAccountingService ACCOUNTING_SERVICE = new AccountingServiceImpl();

    private static final int TOTAL_SUNBEDS = 50;

    public static IMainController buildController() {
        SUNBED_SERVICE.buySunbed(TOTAL_SUNBEDS);
        return new MainControllerImpl(guestService(), sunbedService(),
                receptionService(), accountingService());
    }

    public static IGuestService guestService() {
        return GUEST_SERVICE;
    }


    public static ISunbedService sunbedService() {
        return SUNBED_SERVICE;
    }

    public static IReceptionService receptionService() {
        return RECEPTION_SERVICE;
    }

    public static IAccountingService accountingService() {
        return ACCOUNTING_SERVICE;
    }

    public static IValidationService validationService() {
        return VALIDATION_SERVICE;
    }

    public static ISunbedRepository sunbedRepository() {
        return SUNBED_REPOSITORY;
    }
}
