package vehicles;

import exceptions.InvalidOperationException;
import exceptions.OverloadException;
import exceptions.InsufficientFuelException;
import interfaces.CargoCarrier;
import interfaces.Maintainable;
import interfaces.FuelConsumable;

public class CargoShip extends WaterVehicle implements CargoCarrier, Maintainable, FuelConsumable {

    private final double cargoCapacity;
    private double currentCargo;
    private boolean maintenanceNeeded;
    private double fuelLevel;


    public CargoShip(String id, String model, double maxSpeed, boolean hasSail) throws InvalidOperationException {
        super(id, model, maxSpeed, hasSail);

        this.cargoCapacity = 50000.0;
        this.currentCargo = 0.0;
        this.maintenanceNeeded = false;
        this.fuelLevel = hasSail ? 0.0 : 0.0; // Fuel level only used if hasSail is false
    }

    @Override
    public void move(double distance) throws InvalidOperationException {
        if (distance < 0) {
            throw new InvalidOperationException("Distance cannot be negative.");
        }
        if (!hasSail() && fuelLevel <= 0) {
            throw new InvalidOperationException("Cannot move: no fuel available.");
        }
        if (!hasSail()) {
            try {
                consumeFuel(distance);
            } catch (InsufficientFuelException e) {
                throw new InvalidOperationException("Not enough fuel to sail the cargo ship.");
            }
        }
        updateMileage(distance);
        System.out.println("Sailing with cargo for " + distance + " km...");
    }

    @Override
    public double calculateFuelEfficiency() {
        if (hasSail()) return 0.0;
        return 4.0;
    }

    @Override
    public void loadCargo(double weight) throws OverloadException {
        if (currentCargo + weight > cargoCapacity) {
            throw new OverloadException("Cannot load cargo. Exceeds capacity.");
        }
        currentCargo += weight;
        System.out.println(weight + " kg loaded. Current cargo: " + currentCargo + " kg");
    }

    @Override
    public void unloadCargo(double weight) throws InvalidOperationException {
        if (weight > currentCargo) {
            throw new InvalidOperationException("Cannot unload more than current cargo.");
        }
        currentCargo -= weight;
        System.out.println(weight + " kg unloaded. Current cargo: " + currentCargo + " kg");
    }

    @Override
    public double getCargoCapacity() {
        return cargoCapacity;
    }

    @Override
    public double getCurrentCargo() {
        return currentCargo;
    }

    @Override
    public void scheduleMaintenance() {
        this.maintenanceNeeded = true;
        System.out.println("Maintenance scheduled for CargoShip ID: " + getId());
    }

    @Override
    public boolean needsMaintenance() {
        return maintenanceNeeded || this.maintance() > 10000;
    }

    @Override
    public void performMaintenance() {
        this.maintenanceNeeded = false;
        this.setmaintance(0);
        System.out.println("Maintenance performed for CargoShip ID: " + getId());
    }

    @Override
    public void displayInfo() {
        super.displayInfo();
        System.out.println("Cargo Capacity: " + cargoCapacity + " kg");
        System.out.println("Current Cargo: " + currentCargo + " kg");
        if (!hasSail()) {
            System.out.println("Fuel Level: " + fuelLevel + " liters");
        }
        System.out.println("Needs Maintenance: " + needsMaintenance());
    }

    @Override
    public void refuel(double amount) throws InvalidOperationException {
        if (hasSail()) {
            throw new InvalidOperationException("This ship uses sails, refueling not applicable.");
        }
        if (amount <= 0) {
            throw new InvalidOperationException("Refuel amount must be positive.");
        }
        fuelLevel += amount;
        System.out.println("Refueled " + amount + " liters. Current fuel: " + fuelLevel + " liters");
    }

    @Override
    public double getFuelLevel() {
        if (hasSail()) return 0.0;
        return fuelLevel;
    }

    @Override
    public double consumeFuel(double distance) throws InsufficientFuelException {
        if (hasSail()) return 0.0;
        double required = distance / calculateFuelEfficiency();
        if (fuelLevel < required) {
            throw new InsufficientFuelException("Insufficient fuel. Needed: " + required + " liters");
        }
        fuelLevel -= required;
        return required;
    }
}
