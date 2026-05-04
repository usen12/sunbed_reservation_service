# Sunbeds Reservation System

A robust console-based Java application for managing sunbed reservations at a beach club. The system features sophisticated placement algorithms, automated rental tracking, and detailed accounting.

---

## 🏖️ Project Overview

The beach club features **50 sunbeds arranged in a circular layout**. Guests arrive in groups and request sunbeds for a specific duration. The system prioritizes keeping groups together in consecutive blocks but offers intelligent "split seating" if a single large block is unavailable.

---

## ✨ Key Features

- **Circular Arrangement** — The 50 sunbeds are treated as a circular buffer, allowing groups to wrap around from the last sunbed to the first.
- **Sophisticated Placement** — Prioritizes consecutive seating with random starting points to ensure natural distribution across the beach.
- **Smart Split Seating** — If a group cannot sit together, the system greedily fills the largest available blocks of free sunbeds to keep sub-groups as large as possible.
- **Automated Rental Expiry** — A dedicated background daemon thread monitors rentals and automatically frees sunbeds when time is up.
- **Comprehensive Accounting** — Real-time tracking of revenue, total guests, and "Lost Revenue" (potential earnings from groups turned away due to lack of space).
- **Manual Management** — Staff can manually clear groups by ID or trigger an immediate expiry check.

---

## 🧮 Algorithms & Logic

### 1. Circular Consecutive Placement
To avoid "crowding" at the start of the list, the system picks a random starting index $i$ in the range $[0, 49]$. It then scans $N$ sunbeds in a circular fashion using modulo arithmetic: `index = (i + step) % 50`.
- **Constraint**: All $N$ sunbeds in the sequence must be free.
- **Benefit**: Even distribution of guests and realistic wrap-around seating.

### 2. Greedy Block-Filling (Split Seating)
When no single block of $N$ sunbeds exists, the system uses a greedy strategy:
1.  Identify all contiguous free blocks of sunbeds (accounting for the circular wrap-around).
2.  Sort these blocks by length in descending order.
3.  Greedily fill the largest blocks first until the entire group is accommodated.
- **Goal**: Minimize the number of sub-groups and keep guests as close as possible.

### 3. Background Auto-Expiry
The `Application` class initializes a `ScheduledExecutorService` running a daemon thread. 
- **Frequency**: Every 60 seconds.
- **Action**: Iterates through occupied sunbeds, compares `LocalDateTime.now()` with the guest's `endOfOccupyingTime`, and frees expired slots.

---

## 🏗️ Architecture & Design Patterns

### Design Patterns
- **Builder Pattern**: Used in `Sunbed` and `Guest` models to handle complex object instantiation cleanly.
- **Factory Pattern**: A centralized `Factory` class handles dependency injection and wires together the controllers, services, and repositories.
- **Singleton-like Services**: Services are managed as static final instances within the Factory to maintain state consistency (especially for in-memory repositories).

### Component Breakdown
- **`IMainController`**: The facade for the UI; coordinates between guest, sunbed, and accounting services.
- **`IReceptionService`**: The core "brain" where placement and block-finding algorithms reside.
- **`ISunbedService`**: Manages the state of the 50 sunbeds and handles updates/deletions.
- **`IGuestService`**: Handles the logic of welcoming new groups and assigning rental properties.
- **`IAccountingService`**: Encapsulates all financial logic and statistics gathering.

---

## 🚀 Getting Started

### Requirements
- Java 11 or higher
- Maven 3.6+

### Build & Run
```bash
# Run tests to verify the algorithms
mvn test

# Start the application
mvn compile exec:java -Dexec.mainClass="com.makhabatusen.Application"
```

---

## ⌨️ Console Commands

| Command | Action |
| :--- | :--- |
| `acc` | **Accommodate**: Triggers the placement logic for a new group. |
| `info` | **Occupancy**: Shows a summary and detailed list of all sunbeds. |
| `stats` | **Accounting**: Displays revenue, guest counts, and lost potential. |
| `delete` | **Manual Clear**: Frees a specific group of sunbeds by Group ID. |
| `check` | **Manual Expiry**: Manually triggers the rent-time check. |
| `help` | **Help**: Displays the command list. |
| `exit` | **Shutdown**: Stops the scheduler and exits the app. |

---

## 📂 Project Structure

```text
src/main/java/com/makhabatusen/
├── controller/     # Interface-based controllers (MainControllerImpl)
├── services/       # Core business logic
│   ├── reception/  # ALGORITHMS: Placement & Block-finding
│   ├── sunbed/     # Sunbed management
│   ├── accounting/ # Financial tracking
│   └── ...         # Validation and Guest services
├── models/         # Data models using Builder pattern
├── repository/     # In-memory storage
└── factory/        # Dependency injection & wiring
```
