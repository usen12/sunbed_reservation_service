package com.makhabatusen.repository.guest;

import com.makhabatusen.models.Guest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InMemoryGuestRepository implements IGuestRepository {
    private final List<Guest> guests = new ArrayList<>();

    @Override
    public Guest save(Guest entity) {
        for (int i = 0; i < guests.size(); i++) {
            if (guests.get(i).getId() == entity.getId()) {
                guests.set(i, entity);
                return entity;
            }
        }
        guests.add(entity);
        return entity;
    }

    @Override
    public Optional<Guest> findById(Integer id) {
        return guests.stream()
                .filter(g -> g.getId() == id)
                .findFirst();
    }

    @Override
    public List<Guest> findAll() {
        return new ArrayList<>(guests);
    }

    @Override
    public void deleteById(Integer id) {
        guests.removeIf(g -> g.getId() == id);
    }

    @Override
    public void deleteAll() {
        guests.clear();
    }
}
