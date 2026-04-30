package com.makhabatusen.services.validation;

import com.makhabatusen.services.base.IBaseService;

public interface IValidationService extends IBaseService {
    void validateGuestCount(int count);
    void validateRentHours(int hours);
}
