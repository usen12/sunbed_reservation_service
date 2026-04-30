package com.makhabatusen.services.reception;

import com.makhabatusen.models.Guest;
import com.makhabatusen.models.Sunbed;
import com.makhabatusen.utils.StandardMessages;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class ReceptionServiceImpl implements IReceptionService {

    private final Random random = new Random();

    @Override
    public List<Sunbed> reservedListOfSunbeds(List<Sunbed> sunbeds, List<Guest> guests) {
        logInfo(StandardMessages.DIVIDER);

        if (sunbeds.isEmpty() || guests.isEmpty()) {
            logInfo(StandardMessages.NO_SPACE);
            return new ArrayList<>();
        }

        int needed = guests.size();

        // Single guest: pick any random free sunbed
        if (needed == 1) {
            List<Sunbed> free = sunbeds.stream()
                    .filter(s -> !s.isOccupied())
                    .collect(Collectors.toList());
            if (free.isEmpty()) {
                logInfo(StandardMessages.NO_SPACE);
                return new ArrayList<>();
            }
            Sunbed chosen = free.get(random.nextInt(free.size()));
            logInfo("We have a sunbed for you! Sunbed: " + chosen.getId() + " is assigned.");
            List<Sunbed> result = new ArrayList<>();
            result.add(chosen);
            return result;
        }

        // Group: need 'needed' consecutive free sunbeds in the circular arrangement.
        // Fast-fail: not enough free sunbeds at all.
        long totalFree = sunbeds.stream().filter(s -> !s.isOccupied()).count();
        if (totalFree < needed) {
            logInfo(StandardMessages.NO_SPACE);
            return new ArrayList<>();
        }

        int n = sunbeds.size();
        // Start the circular search from a random position so groups spread naturally.
        int randomStart = random.nextInt(n);

        List<Sunbed> preserveList = new ArrayList<>();
        int desiredNumber = needed;

        for (int i = randomStart; i < randomStart + n * 2; i++) {
            int currentIdx = i % n;
            int nextIdx = (i + 1) % n;

            boolean currentFree = !sunbeds.get(currentIdx).isOccupied();
            boolean nextFree = !sunbeds.get(nextIdx).isOccupied();

            // For the last sunbed needed we only require the current slot to be free,
            // not the next one, since we are not extending the sequence further.
            if (currentFree && (nextFree || desiredNumber == 1)) {
                preserveList.add(sunbeds.get(currentIdx));
                desiredNumber--;
            } else {
                desiredNumber = needed;
                preserveList.clear();
            }

            if (desiredNumber == 0) {
                logInfo("We have enough sunbeds for your group!");
                preserveList.forEach(s -> logInfo("Sunbed: " + s.getId() + " is assigned."));
                return preserveList;
            }
        }

        logInfo(StandardMessages.NO_SPACE);
        return new ArrayList<>();
    }

    @Override
    public boolean canAccommodateConsecutively(List<Sunbed> sunbeds, int numberOfGuests) {
        if (sunbeds.isEmpty() || numberOfGuests <= 0) return false;
        if (numberOfGuests == 1) return sunbeds.stream().anyMatch(s -> !s.isOccupied());

        long totalFree = sunbeds.stream().filter(s -> !s.isOccupied()).count();
        if (totalFree < numberOfGuests) return false;

        int n = sunbeds.size();
        int consecutive = 0;
        // Walk once around the circle plus (count-1) extra steps to handle the wrap-around case.
        for (int i = 0; i < n + numberOfGuests - 1; i++) {
            int idx = i % n;
            int nextIdx = (i + 1) % n;
            boolean currentFree = !sunbeds.get(idx).isOccupied();
            boolean nextFree = !sunbeds.get(nextIdx).isOccupied();
            if (currentFree && (nextFree || consecutive + 1 == numberOfGuests)) {
                consecutive++;
                if (consecutive == numberOfGuests) return true;
            } else {
                consecutive = 0;
            }
        }
        return false;
    }

    @Override
    public List<Sunbed> reserveInBestBlocks(List<Sunbed> sunbeds, int count) {
        List<int[]> blocks = getAllFreeBlocks(sunbeds);
        List<Sunbed> result = new ArrayList<>();
        int remaining = count;

        for (int[] block : blocks) {
            int take = Math.min(remaining, block[1]);
            for (int i = 0; i < take; i++) {
                result.add(sunbeds.get((block[0] + i) % sunbeds.size()));
            }
            remaining -= take;
            if (remaining == 0) break;
        }
        return result;
    }

    // Returns all consecutive free blocks sorted by length descending.
    // Each block is int[]{startIndex, length}.
    // Starts scanning from just after the first occupied sunbed so that a free
    // block wrapping around the end of the array (e.g. indices 48-49-0-1) is
    // counted as one contiguous run rather than two fragments.
    private List<int[]> getAllFreeBlocks(List<Sunbed> sunbeds) {
        int n = sunbeds.size();

        if (sunbeds.stream().noneMatch(Sunbed::isOccupied)) {
            List<int[]> all = new ArrayList<>();
            all.add(new int[]{0, n});
            return all;
        }

        int start = 0;
        for (int i = 0; i < n; i++) {
            if (sunbeds.get(i).isOccupied()) {
                start = (i + 1) % n;
                break;
            }
        }

        List<int[]> blocks = new ArrayList<>();
        int blockStart = -1;
        int blockLen = 0;

        for (int i = 0; i < n; i++) {
            int idx = (start + i) % n;
            if (!sunbeds.get(idx).isOccupied()) {
                if (blockLen == 0) blockStart = idx;
                blockLen++;
            } else {
                if (blockLen > 0) {
                    blocks.add(new int[]{blockStart, blockLen});
                    blockLen = 0;
                }
            }
        }
        if (blockLen > 0) {
            blocks.add(new int[]{blockStart, blockLen});
        }

        blocks.sort((a, b) -> b[1] - a[1]);
        return blocks;
    }
}
