package com.makhabatusen.repository.sunbed;

import com.makhabatusen.models.Sunbed;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InMemorySunbedRepository implements ISunbedRepository {
    private final List<Sunbed> sunbeds = new ArrayList<>();

    @Override
    public Sunbed save(Sunbed entity) {
        for (int i = 0; i < sunbeds.size(); i++) {
            if (sunbeds.get(i).getId() == entity.getId()) {
                sunbeds.set(i, entity);
                return entity;
            }
        }
        sunbeds.add(entity);
        return entity;
    }

    @Override
    public Optional<Sunbed> findById(Integer id) {
        return sunbeds.stream()
                .filter(s -> s.getId() == id)
                .findFirst();
    }

    @Override
    public List<Sunbed> findAll() {
        return new ArrayList<>(sunbeds);
    }

    @Override
    public void deleteById(Integer id) {
        sunbeds.removeIf(s -> s.getId() == id);
    }

    @Override
    public void deleteAll() {
        sunbeds.clear();
    }
}
