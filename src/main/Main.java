package main;

import fleet.FleetManager;
import vehicles.*;
import interfaces.FuelConsumable;
import interfaces.Maintainable;
import exceptions.InvalidOperationException;

import java.util.Scanner;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        FleetManager manager = new FleetManager();
        Scanner scanner = new Scanner(System.in);

        // Demo: create sample vehicles and add to fleet
        try {
            manager.addVehicle(new Car("C001", "Toyota", 120.0, 4));
            manager.addVehicle(new Truck("T001", "Volvo", 100.0, 6));
            manager.addVehicle(new Bus("B001", "Mercedes", 80.0, 6));
            manager.addVehicle(new Airplane("A001", "Boeing", 800.0, 10000.0));
            manager.addVehicle(new CargoShip("S001", "Titanic", 50.0, true));
        } catch (InvalidOperationException e) {
            System.out.println("Error adding demo vehicles: " + e.getMessage());
        }

        boolean exit = false;

        while (!exit) {
            System.out.println("\n--- Fleet Management Menu ---");
            System.out.println("1. Add Vehicle");
            System.out.println("2. Remove Vehicle");
            System.out.println("3. Move All vehichle");
            System.out.println("4. Refuel All Fuel Vehicles");
            System.out.println("5. Perform Maintenance");
            System.out.println("6. Generate Report");
            System.out.println("7. Save Fleet");
            System.out.println("8. Load Fleet");
            System.out.println("9. Search by Type");
            System.out.println("10. List Vehicles Needing Maintenance");
            System.out.println("11. Perform Maintenance for specific vehicle ID");
            System.out.println("12. Start Journey");
            System.out.println("13. Manage Cargo/Passengers");
            System.out.println("14. Exit");
            System.out.print("Enter choice: ");

            int choice = -1;
            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                scanner.nextLine(); // consume newline
            } else {
                scanner.nextLine();
                System.out.println("Invalid input. Enter a number 1-11.");
                continue;
            }

            try {
                switch (choice) {
                    case 1 -> addVehicleCLI(manager, scanner);
                    case 2 -> {
                        System.out.print("Enter vehicle ID to remove: ");
                        String removeId = scanner.nextLine();
                        manager.removeVehicle(removeId);
                        System.out.println("Vehicle removed.");
                    }
                    case 3 -> {
                        System.out.print("Enter distance to travel: ");
                        double distance = scanner.nextDouble();
                        scanner.nextLine();
                        manager.startAllJourneys(distance);
                    }
                    case 4 -> {
                        System.out.print("Enter fuel amount to refuel all: ");
                        double amount = scanner.nextDouble();
                        scanner.nextLine();
                        for (Vehicle v : manager.searchByType(Vehicle.class)) {
                            if (v instanceof FuelConsumable f) {
                                try {
                                    f.refuel(amount);
                                } catch (InvalidOperationException e) {
                                    System.out.println(e.getMessage());
                                }
                            }
                        }
                    }
                    case 5 -> manager.maintainAll();
                    case 6 -> System.out.println(manager.generateReport());
                    case 7 -> {
                        System.out.print("Enter filename to save fleet: ");
                        String saveFile = scanner.nextLine();
                        manager.saveToFile(saveFile);
                    }
                    case 8 -> {
                        System.out.print("Enter filename to load fleet: ");
                        String loadFile = scanner.nextLine();
                        manager.loadFromFile(loadFile);
                    }
                    case 9 -> {
                        System.out.print("Enter type to search (Car/Truck/Bus/Airplane/CargoShip): ");
                        String typeName = scanner.nextLine();

                        List<Vehicle> results = manager.searchByTypeName(typeName);

                        if (!results.isEmpty()) {
                            System.out.println();
                            System.out.println("Found " + results.size() + " vehicle(s):");
                            System.out.println();
                            for (Vehicle v : results) {
                                v.displayInfo();
                                System.out.println();                   // blank line
                                System.out.println("----------------------"); // separator line
                                System.out.println();
                            }
                        } else {
                            System.out.println("No vehicles found of type " + typeName);
                        }

                    }
                    case 10 -> {
                        List<Vehicle> maintenanceList = manager.getVehiclesNeedingMaintenance();
                        System.out.println("Vehicles needing maintenance:");
                        System.out.println("----------------------");
                        System.out.println();
                        for (Vehicle v : maintenanceList) {
                            v.displayInfo();
                            System.out.println();                   // blank line
                            System.out.println("----------------------"); // separator line
                            System.out.println();
                        }

                    }
                    case 14 -> {
                        exit = true;
                        System.out.println("Exiting Fleet Management System.");
                    }
                    case 11 -> {
                        Scanner scanner1 = new Scanner(System.in);  // Create Scanner object

                        System.out.print("Enter a string: ");
                        String input = scanner.nextLine();

                        manager.maintainone(input);
                    }
                    case 12 -> {
                        Scanner sc = new Scanner(System.in);

                        System.out.print("Enter a distance (double): ");
                        double distance = sc.nextDouble();


                        manager.planRoute(distance);
                    }case 13 -> {
                        manager.manageCargoAndPassengers();
                    }
                    default -> System.out.println("Invalid choice. Enter 1-11.");
                }
            } catch (InvalidOperationException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        scanner.close();
    }

    private static void addVehicleCLI(FleetManager manager, Scanner scanner) {
        System.out.print("Enter vehicle type (Car/Truck/Bus/Airplane/CargoShip): ");
        String type = scanner.nextLine();

        System.out.print("Enter vehicle ID: ");
        String id = scanner.nextLine();

        System.out.print("Enter vehicle model: ");
        String model = scanner.nextLine();

        System.out.print("Enter max speed: ");
        double maxSpeed = scanner.nextDouble();
        scanner.nextLine();

        try {
            Vehicle v = null;
            switch (type) {
                case "Car" -> v = new Car(id, model, maxSpeed, 4);
                case "Truck" -> v = new Truck(id, model, maxSpeed, 6);
                case "Bus" -> v = new Bus(id, model, maxSpeed, 6);
                case "Airplane" -> {
                    System.out.print("Enter max altitude: ");
                    double alt = scanner.nextDouble();
                    scanner.nextLine();
                    v = new Airplane(id, model, maxSpeed, alt);
                }
                case "CargoShip" -> {
                    System.out.print("Has sail? true/false: ");
                    boolean hasSail = scanner.nextBoolean();
                    scanner.nextLine();
                    v = new CargoShip(id, model, maxSpeed, hasSail);
                }
                default -> System.out.println("Invalid vehicle type.");
            }

            if (v != null) {
                manager.addVehicle(v);
                System.out.println(type + " added successfully.");
            }
        } catch (InvalidOperationException e) {
            System.out.println("Error adding vehicle: " + e.getMessage());
        }
    }
}
