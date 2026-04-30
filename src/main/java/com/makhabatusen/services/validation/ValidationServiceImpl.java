package com.makhabatusen.services.validation;

import com.makhabatusen.exceptions.ValidationException;

public class ValidationServiceImpl implements IValidationService {
    @Override
    public void validateGuestCount(int count) {
        if (count <= 0) {
            throw new ValidationException("Guest count must be positive. Provided: " + count);
        }
        if (count > 50) {
            throw new ValidationException("Group size cannot exceed 50 people. Provided: " + count);
        }
    }

    @Override
    public void validateRentHours(int hours) {
        if (hours <= 0) {
            throw new ValidationException("Rent hours must be positive. Provided: " + hours);
        }
        if (hours > 24) {
            throw new ValidationException("Rent duration cannot exceed 24 hours. Provided: " + hours);
        }
    }

}
