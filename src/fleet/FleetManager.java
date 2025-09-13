package fleet;

import vehicles.Vehicle;
import vehicles.Car;
import vehicles.Truck;
import vehicles.Bus;
import vehicles.Airplane;
import vehicles.CargoShip;

import interfaces.FuelConsumable;
import interfaces.Maintainable;
import interfaces.CargoCarrier;
import interfaces.PassengerCarrier;

import exceptions.InvalidOperationException;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.Scanner;

public class FleetManager {
    private List<Vehicle> fleet;

    public FleetManager() {
        fleet = new ArrayList<>();
    }

    public void addVehicle(Vehicle v) throws InvalidOperationException {
        for (Vehicle vehicle : fleet) {
            if (vehicle.getId().equalsIgnoreCase(v.getId())) {
                throw new InvalidOperationException("Duplicate vehicle ID: " + v.getId());
            }
        }
        fleet.add(v);
    }

    public void removeVehicle(String id) throws InvalidOperationException {
        boolean removed = fleet.removeIf(v -> v.getId().equals(id));
        if (!removed) {
            throw new InvalidOperationException("Vehicle ID not found: " + id);
        }
    }

    public void startAllJourneys(double distance) {
        for (Vehicle v : fleet) {
            try {
                v.move(distance);
            } catch (InvalidOperationException e) {
                System.out.println("Error moving vehicle ID " + v.getId() + ": " + e.getMessage());
            }
        }
    }

    public double getTotalFuelConsumption(double distance) {
        double total = 0.0;
        for (Vehicle v : fleet) {
            if (v instanceof FuelConsumable) {
                try {
                    total += ((FuelConsumable) v).consumeFuel(distance);
                } catch (Exception e) {
                    System.out.println("Vehicle ID " + v.getId() + ": " + e.getMessage());
                }
            }
        }
        return total;
    }

    public void maintainAll() {
        for (Vehicle v : fleet) {
            if (v instanceof Maintainable m && m.needsMaintenance()) {
                m.performMaintenance();
            }
        }
    }
    public void maintainone(String id1) {
        for (Vehicle v : fleet) {
            if (v instanceof Maintainable m && m.needsMaintenance() && v.getId().equals(id1)) {
                m.performMaintenance();
            }
        }
    }

    public List<Vehicle> searchByType(Class<?> type) {
        List<Vehicle> result = new ArrayList<>();
        for (Vehicle v : fleet) {
            if (type.isInstance(v)) {
                result.add(v);
            }
        }
        return result;
    }

    public void sortFleetByEfficiency() {
        Collections.sort(fleet);
    }

    public String generateReport() {
        StringBuilder sb = new StringBuilder();
        sb.append("Fleet Report:\n");
        sb.append("Total Vehicles: ").append(fleet.size()).append("\n");

        sb.append("Count by Type:\n");
        for (String type : List.of("Car", "Truck", "Bus", "Airplane", "CargoShip")) {
            long count = fleet.stream().filter(v -> v.getClass().getSimpleName().equals(type)).count();
            if (count > 0) sb.append(" - ").append(type).append(": ").append(count).append("\n");
        }

        double totalEfficiency = 0.0;
        double totalMileage = 0.0;
        for (Vehicle v : fleet) {
            totalEfficiency += v.calculateFuelEfficiency();
            totalMileage += v.getCurrentMileage();
        }

        sb.append(String.format("Average Efficiency: %.2f km/l\n", fleet.isEmpty() ? 0.0 : totalEfficiency / fleet.size()));
        sb.append(String.format("Total Mileage: %.2f km\n", totalMileage));

        sb.append("Vehicles Needing Maintenance:\n");
        for (Vehicle v : getVehiclesNeedingMaintenance()) {
            sb.append(" - ").append(v.getClass().getSimpleName())
                    .append(" (ID: ").append(v.getId()).append(")\n");
        }

        return sb.toString();
    }

    public List<Vehicle> getVehiclesNeedingMaintenance() {
        List<Vehicle> result = new ArrayList<>();
        for (Vehicle v : fleet) {
            if (v instanceof Maintainable m && m.needsMaintenance()) {
                result.add(v);
            }
        }
        return result;
    }

    // Save Fleet to CSV
    public void saveToFile(String filename) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            for (Vehicle v : fleet) {
                StringBuilder sb = new StringBuilder();
                if (v instanceof Car c) {
                    sb.append("Car,")
                            .append(c.getId()).append(",")
                            .append(c.getModel()).append(",")
                            .append(c.getMaxSpeed()).append(",")
                            .append(c.getCurrentMileage()).append(",")
                            .append(c.getNumberOfWheels()).append(",")
                            .append(c.getFuelLevel()).append(",")
                            .append(c.getPassengerCapacity()).append(",")
                            .append(c.maintance()).append(",")
                            .append(c.getCurrentPassengers());
                } else if (v instanceof Truck t) {
                    sb.append("Truck,")
                            .append(t.getId()).append(",")
                            .append(t.getModel()).append(",")
                            .append(t.getMaxSpeed()).append(",")
                            .append(t.getCurrentMileage()).append(",")
                            .append(t.getNumberOfWheels()).append(",")
                            .append(t.getFuelLevel()).append(",")
                            .append(t.getCargoCapacity()).append(",")
                            .append(t.maintance()).append(",")
                            .append(t.getCurrentCargo());
                } else if (v instanceof Bus b) {
                    sb.append("Bus,")
                            .append(b.getId()).append(",")
                            .append(b.getModel()).append(",")
                            .append(b.getMaxSpeed()).append(",")
                            .append(b.getCurrentMileage()).append(",")
                            .append(b.getNumberOfWheels()).append(",")
                            .append(b.getFuelLevel()).append(",")
                            .append(b.getPassengerCapacity()).append(",")
                            .append(b.getCurrentPassengers()).append(",")
                            .append(b.getCargoCapacity()).append(",")
                            .append(b.maintance()).append(",")
                            .append(b.getCurrentCargo());
                } else if (v instanceof CargoShip c) {
                    sb.append("CargoShip,")
                            .append(c.getId()).append(",")
                            .append(c.getModel()).append(",")
                            .append(c.getMaxSpeed()).append(",")
                            .append(c.getCurrentMileage()).append(",")
                            .append(c.hasSail()).append(",")
                            .append(c.getFuelLevel()).append(",")
                            .append(c.getCargoCapacity()).append(",")
                            .append(c.maintance()).append(",")
                            .append(c.getCurrentCargo());
                } else if (v instanceof Airplane a) {
                    sb.append("Airplane,")
                            .append(a.getId()).append(",")
                            .append(a.getModel()).append(",")
                            .append(a.getMaxSpeed()).append(",")
                            .append(a.getCurrentMileage()).append(",")
                            .append(a.getMaxAltitude()).append(",")
                            .append(a.getFuelLevel()).append(",")
                            .append(a.getPassengerCapacity()).append(",")
                            .append(a.maintance()).append(",")
                            .append(a.getCurrentPassengers());
                }
                writer.println(sb.toString());
            }
            System.out.println("Fleet saved to " + filename);
        } catch (IOException e) {
            System.out.println("Error saving to CSV: " + e.getMessage());
        }
    }

    // Load Fleet from CSV using factory method inside this class
    public void loadFromFile(String filename) {
        fleet.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                try {
                    Vehicle v = createVehicleFromParts(parts);
                    fleet.add(v);
                } catch (Exception e) {
                    System.out.println("⚠️ Error loading vehicle " + (parts.length > 1 ? parts[1] : "") + ": " + e.getMessage());
                }
            }
            System.out.println("Fleet loaded from " + filename);
        } catch (IOException e) {
            System.out.println("Error reading CSV: " + e.getMessage());
        }
    }

    // Factory method: recreate vehicle from CSV parts
    private Vehicle createVehicleFromParts(String[] parts) throws Exception {
        if (parts == null || parts.length == 0) {
            throw new IllegalArgumentException("Empty CSV line");
        }

        String type = parts[0];

        return switch (type) {
            case "Car" -> {
                Car car = new Car(parts[1], parts[2], Double.parseDouble(parts[3]), Integer.parseInt(parts[5]));
                car.setCurrentMileage(Double.parseDouble(parts[4]));
                car.setmaintance(Double.parseDouble(parts[8]));
                double fuel = Double.parseDouble(parts[6]);
                if (fuel > 0) car.refuel(fuel);
                int passengers = Integer.parseInt(parts[9]);
                if (passengers > 0) car.boardPassengers(passengers);
                yield car;
            }
            case "Truck" -> {
                Truck truck = new Truck(parts[1], parts[2], Double.parseDouble(parts[3]), Integer.parseInt(parts[5]));
                truck.setCurrentMileage(Double.parseDouble(parts[4]));
                truck.setmaintance(Double.parseDouble(parts[8]));
                double fuel = Double.parseDouble(parts[6]);
                if (fuel > 0) truck.refuel(fuel);
                double cargo = Double.parseDouble(parts[9]);
                if (cargo > 0) truck.loadCargo(cargo);
                yield truck;
            }
            case "Bus" -> {
                Bus bus = new Bus(parts[1], parts[2], Double.parseDouble(parts[3]), Integer.parseInt(parts[5]));
                bus.setCurrentMileage(Double.parseDouble(parts[4]));
                bus.setmaintance(Double.parseDouble(parts[10]));
                double fuel = Double.parseDouble(parts[6]);
                if (fuel > 0) bus.refuel(fuel);
                int passengers = Integer.parseInt(parts[8]);
                if (passengers > 0) bus.boardPassengers(passengers);
                double cargo = Double.parseDouble(parts[11]);
                if (cargo > 0) bus.loadCargo(cargo);
                yield bus;
            }
            case "CargoShip" -> {
                CargoShip cs = new CargoShip(parts[1], parts[2], Double.parseDouble(parts[3]), Boolean.parseBoolean(parts[5]));
                cs.setCurrentMileage(Double.parseDouble(parts[4]));
                cs.setmaintance(Double.parseDouble(parts[8]));
                double fuel = Double.parseDouble(parts[6]);
                if (fuel > 0) cs.refuel(fuel);
                double cargo = Double.parseDouble(parts[9]);
                if (cargo > 0) cs.loadCargo(cargo);
                yield cs;
            }
            case "Airplane" -> {
                Airplane ap = new Airplane(parts[1], parts[2], Double.parseDouble(parts[3]), Double.parseDouble(parts[5]));
                ap.setCurrentMileage(Double.parseDouble(parts[4]));
                ap.setmaintance(Double.parseDouble(parts[8]));
                double fuel = Double.parseDouble(parts[6]);
                if (fuel > 0) ap.refuel(fuel);
                int passengers = Integer.parseInt(parts[9]);
                if (passengers > 0) ap.boardPassengers(passengers);
                yield ap;
            }
            default -> throw new IllegalArgumentException("Unknown vehicle type: " + type);
        };
    }


    public void planRoute(double distance) {
        System.out.println("=== Route Planning for " + distance + " km ===");

        // 1. Display all vehicles with journey details
        for (Vehicle v : fleet) {
            try {
                double time = v.estimateJourneyTime(distance);
                double efficiency = v.calculateFuelEfficiency();

                double fuelUsed = 0.0;
                double currentFuel = -1; // default if not FuelConsumable

                if (v instanceof FuelConsumable f) {
                    fuelUsed = distance / efficiency;
                    currentFuel = f.getFuelLevel();
                }

                System.out.printf("%s (ID: %s): Time = %.2f hrs, Fuel Needed = %.2f L",
                        v.getClass().getSimpleName(), v.getId(), time, fuelUsed);

                if (currentFuel >= 0) {
                    System.out.printf(", Current Fuel = %.2f L%n", currentFuel);
                } else {
                    System.out.println(" (Not fuel-based vehicle)");
                }

            } catch (Exception e) {
                System.out.println("Error planning route for vehicle ID " + v.getId() + ": " + e.getMessage());
            }
        }

        // 2. Ask user which vehicle to use
        Scanner sc1 = new Scanner(System.in);
        System.out.print("Enter the ID of the vehicle to perform this journey: ");
        String chosenId = sc1.nextLine();

        Vehicle chosenVehicle = null;
        for (Vehicle v : fleet) {
            if (v.getId().equalsIgnoreCase(chosenId)) {
                chosenVehicle = v;
                break;
            }
        }

        if (chosenVehicle == null) {
            System.out.println("No vehicle with ID " + chosenId + " found!");
            return;
        }

        // 3. Simulate the actual journey
        try {
            chosenVehicle.move(distance); // updates mileage + fuel internally
            // updates mileage + fuel internally
            System.out.println("Journey completed with vehicle ID: " + chosenVehicle.getId());
            chosenVehicle.displayInfo();

            if (chosenVehicle instanceof FuelConsumable f) {
                System.out.printf("Remaining fuel: %.2f L%n", f.getFuelLevel());
            }
        } catch (Exception e) {
            System.out.println("Journey failed: " + e.getMessage());
        }
    }


    public void manageCargoAndPassengers() {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter Vehicle ID: ");
        String id = sc.nextLine();

        Vehicle chosen = null;
        for (Vehicle v : fleet) {
            if (v.getId().equalsIgnoreCase(id)) {
                chosen = v;
                break;
            }
        }

        if (chosen == null) {
            System.out.println("No vehicle with ID " + id + " found!");
            return;
        }

        // Cargo operations
        if (chosen instanceof CargoCarrier cargoVehicle) {
            System.out.println("=== Cargo Options ===");
            System.out.println("1. Load Cargo");
            System.out.println("2. Unload Cargo");
            System.out.println("3. Show Cargo Status");
            System.out.print("Choose: ");
            int choice = sc.nextInt();

            try {
                switch (choice) {
                    case 1 -> {
                        System.out.print("Enter cargo weight to load: ");
                        double w = sc.nextDouble();
                        cargoVehicle.loadCargo(w);
                        System.out.println("Loaded successfully.");
                    }
                    case 2 -> {
                        System.out.print("Enter cargo weight to unload: ");
                        double w = sc.nextDouble();
                        cargoVehicle.unloadCargo(w);
                        System.out.println("Unloaded successfully.");
                    }
                    case 3 -> {
                        System.out.printf("Current Cargo: %.2f / %.2f%n",
                                cargoVehicle.getCurrentCargo(), cargoVehicle.getCargoCapacity());
                    }
                    default -> System.out.println("Invalid option!");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        // Passenger operations
        if (chosen instanceof PassengerCarrier passengerVehicle) {
            System.out.println("=== Passenger Options ===");
            System.out.println("1. Board Passengers");
            System.out.println("2. Disembark Passengers");
            System.out.println("3. Show Passenger Status");
            System.out.print("Choose: ");
            int choice = sc.nextInt();

            try {
                switch (choice) {
                    case 1 -> {
                        System.out.print("Enter number of passengers to board: ");
                        int p = sc.nextInt();
                        passengerVehicle.boardPassengers(p);
                        System.out.println("Passengers boarded.");
                    }
                    case 2 -> {
                        System.out.print("Enter number of passengers to disembark: ");
                        int p = sc.nextInt();
                        passengerVehicle.disembarkPassengers(p);
                        System.out.println("Passengers disembarked.");
                    }
                    case 3 -> {
                        System.out.printf("Current Passengers: %d / %d%n",
                                passengerVehicle.getCurrentPassengers(), passengerVehicle.getPassengerCapacity());
                    }
                    default -> System.out.println("Invalid option!");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        if (!(chosen instanceof CargoCarrier) && !(chosen instanceof PassengerCarrier)) {
            System.out.println("This vehicle does not support cargo or passenger operations.");
        }
    }
    public List<Vehicle> searchByTypeName(String typeName) {
        List<Vehicle> results = new ArrayList<>();

        for (Vehicle v : fleet) {
            if (typeName.equalsIgnoreCase("Car") && v instanceof Car) {
                results.add(v);
            } else if (typeName.equalsIgnoreCase("Truck") && v instanceof Truck) {
                results.add(v);
            } else if (typeName.equalsIgnoreCase("Bus") && v instanceof Bus) {
                results.add(v);
            } else if (typeName.equalsIgnoreCase("Airplane") && v instanceof Airplane) {
                results.add(v);
            } else if (typeName.equalsIgnoreCase("CargoShip") && v instanceof CargoShip) {
                results.add(v);
            }
        }

        return results;
    }




}
