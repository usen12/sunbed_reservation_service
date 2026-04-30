package com.makhabatusen.services.reception;

import com.makhabatusen.models.Guest;
import com.makhabatusen.models.Sunbed;
import com.makhabatusen.services.base.IBaseService;

import java.util.List;

public interface IReceptionService extends IBaseService {

    List<Sunbed> reservedListOfSunbeds(List<Sunbed> sunbeds, List<Guest> guests);

    boolean canAccommodateConsecutively(List<Sunbed> sunbeds, int numberOfGuests);

    List<Sunbed> reserveInBestBlocks(List<Sunbed> sunbeds, int count);

}
