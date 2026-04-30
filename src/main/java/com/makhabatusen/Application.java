package com.makhabatusen;

import com.makhabatusen.controller.IMainController;
import com.makhabatusen.exceptions.AppException;
import com.makhabatusen.factory.Factory;

import java.time.LocalDateTime;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Application {
    private static IMainController mainControllerImpl;
    private static final int EXPIRY_CHECK_INTERVAL_SECONDS = 60;

    public static void main(String[] args) {
        mainControllerImpl = Factory.buildController();
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r, "expiry-checker");
            t.setDaemon(true);
            return t;
        });
        scheduler.scheduleAtFixedRate(() -> {
            if (mainControllerImpl.checkForExpiredRentTime()) {
                System.out.println("\n[AUTO] Expired rentals cleared. Enter command: ");
            }
        }, EXPIRY_CHECK_INTERVAL_SECONDS, EXPIRY_CHECK_INTERVAL_SECONDS, TimeUnit.SECONDS);

        System.out.println("Welcome to the Beach Club Management System!");
        System.out.println("50 sunbeds are ready for guests.");
        printHelp();

        while (!exit) {
            System.out.print("\nEnter command: ");
            String command = scanner.nextLine().trim().toLowerCase();

            switch (command) {
                case "acc":
                    try {
                        System.out.print("Number of guests: ");
                        int guests = Integer.parseInt(scanner.nextLine());
                        System.out.print("Rent hours: ");
                        int hours = Integer.parseInt(scanner.nextLine());
                        LocalDateTime startTime = LocalDateTime.now();

                        int free = mainControllerImpl.getFreeSunbedsCount();
                        if (free < guests) {
                            System.out.println("Sorry, only " + free + " sunbeds are free. Not enough for a group of " + guests + ".");
                            mainControllerImpl.registerRejectedGroup(guests, hours);
                            break;
                        }

                        if (mainControllerImpl.canAccommodateGroupTogether(guests)) {
                            info("Group accommodated: " + mainControllerImpl.isGroupAccommodated(guests, startTime, hours));
                            info(mainControllerImpl.currentOccupancy());
                        } else {
                            System.out.println("We cannot seat all " + guests + " guests together.");
                            System.out.println("There are " + free + " free sunbeds available separately. Would you like to be seated apart? (yes/no)");
                            String answer = scanner.nextLine().trim().toLowerCase();
                            if ("yes".equals(answer)) {
                                info("Group accommodated separately: " + mainControllerImpl.accommodateGroupSplit(guests, startTime, hours));
                                info(mainControllerImpl.currentOccupancy());
                            } else {
                                System.out.println("Reservation cancelled. Hope to see you again!");
                                mainControllerImpl.registerRejectedGroup(guests, hours);
                            }
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid number format.");
                    } catch (AppException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;
                case "check":
                    checkForExpiredTime();
                    break;
                case "info":
                    info(mainControllerImpl.currentOccupancy());
                    mainControllerImpl.sunbedsInfo();
                    break;
                case "stats":
                    info(mainControllerImpl.showStatistics());
                    break;
                case "delete":
                    System.out.print("Enter group ID to delete: ");
                    try {
                        int groupId = Integer.parseInt(scanner.nextLine());
                        deleteGroup(groupId);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid number format.");
                    }
                    break;
                case "help":
                    printHelp();
                    break;
                case "exit":
                    scheduler.shutdownNow();
                    exit = true;
                    System.out.println("Exiting... Have a nice day!");
                    break;
                default:
                    System.out.println("Unknown command. Type 'help' for available commands.");
            }
        }
        scanner.close();
    }

    private static void printHelp() {
        System.out.println("Available commands:");
        System.out.println("  acc    - Accommodate a new group of guests");
        System.out.println("  check  - Check for expired rent time");
        System.out.println("  info   - Show current occupancy info");
        System.out.println("  stats  - Show accounting statistics");
        System.out.println("  delete - Manually delete a group by ID");
        System.out.println("  help   - Show this help message");
        System.out.println("  exit   - Exit application");
    }

    public static void checkForExpiredTime() {
        info("Any Guests with Expired time: " + mainControllerImpl.checkForExpiredRentTime());

    }

    public static void deleteGroup(int groupId) {
        info("Group is deleted " + mainControllerImpl.deleteByGroupNum(groupId));
    }

    public static void info(String message) {
        System.out.println(message);
    }

}

